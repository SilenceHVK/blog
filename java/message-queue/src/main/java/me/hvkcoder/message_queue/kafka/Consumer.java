package me.hvkcoder.message_queue.kafka;


import cn.hutool.core.thread.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Kafka 消费者实例
 *
 * @author h-vk
 * @since 2020-03-26
 */
@Slf4j
public class Consumer {

	static Properties properties;
	static KafkaConsumer<String, String> kafkaConsumer;

	static {
		properties = new Properties();
		// Kafka 服务地址
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		// Key 反序列化类
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// Value 反序列化类
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// Consumer 组名称
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group1");
		// 创建 Consumer
		kafkaConsumer = new KafkaConsumer<>(properties);
	}

	@After
	public void close() {
		kafkaConsumer.close();
	}

	/**
	 * 消费者简单消费
	 */
	@Test
	public void testSimpleConsumer() {
		// 订阅主题，可以使用 Pattern.compile("") 正则表达式
		kafkaConsumer.subscribe(Collections.singleton("topic01"));
		while (true) {
			// 设置间隔时间拉取消息
			final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			consumerRecords.forEach(r -> {
				log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value());
			});
		}
	}

	/**
	 * 首次 Offset 消费策略
	 */
	@Test
	public void testOffsetStrategyConsumer() {
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "offset_strategy");
		// 消费者首次连接 Kafka offset 策略：latest(默认，最新的一条)、earliest（最开始）、none（未找到消费组的offset，则抛出异常）
		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		kafkaConsumer = new KafkaConsumer<String, String>(properties);
		kafkaConsumer.subscribe(Collections.singleton("topic01"));
		while (true) {
			final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			consumerRecords.forEach(r -> log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value()));
		}
	}

	/**
	 * 指定消费 Offset
	 */
	@Test
	public void testAssignOffsetConsumer() {
		final Set<TopicPartition> topicPartitions = Collections.singleton(new TopicPartition("topic01", 0));
		// 手动指定消费分区，该方法将失去组管理特性
		kafkaConsumer.assign(topicPartitions);
		// 从 partition 开始位置消费
//		kafkaConsumer.seekToBeginning(topicPartitions);
		// 从 partition 指定 offset 位置消费
		kafkaConsumer.seek(new TopicPartition("topic01", 0), 36L);
		while (true) {
			final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			consumerRecords.forEach(r -> log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value()));
		}
	}

	/**
	 * 手动提交 Offset
	 */
	@Test
	public void testManualCommitOffsetConsumer() {
		// 关闭 offset 自动提交
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		// 设置自动提交 offset 时间间隔
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);
		kafkaConsumer = new KafkaConsumer<>(properties);
		kafkaConsumer.subscribe(Collections.singleton("topic01"));

		while (true) {
			final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
			if (!consumerRecords.isEmpty()) {
				// 记录分区消费元数据信息
				Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
				consumerRecords.forEach(r -> {
					log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value());
					offsets.put(new TopicPartition(r.topic(), r.partition()), new OffsetAndMetadata(r.offset() + 1));
				});
				// 手动提交 Kafka 分区元数据
				kafkaConsumer.commitAsync(offsets, new OffsetCommitCallback() {
					@Override
					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
						log.info("offsets: {}, exception: {}", offsets, exception);
					}
				});
			}
		}
	}

	/**
	 * 生产者&消费者事务
	 */
	@Test
	public void testTransactionProducerAndConsumer() {
		// 设置消费者的消费事务隔离级别
		properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
		// 消费者使用事务，必须关闭 offset 自动提交
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "transaction_group");

		// 创建 Producer
		final Properties producerProperties = new Properties();
		producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProperties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional-id-" + UUID.randomUUID().toString());
		producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		producerProperties.put(ProducerConfig.ACKS_CONFIG, "all");

		try (
			final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
			final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(producerProperties);
		) {
			kafkaConsumer.subscribe(Collections.singleton("topic01"));
			// 初始化事务
			kafkaProducer.initTransactions();
			while (true) {
				final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
				final Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();
				try {
					kafkaProducer.beginTransaction();
					consumerRecords.forEach(r -> {
						log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value());
						offsets.put(new TopicPartition(r.topic(), r.partition()), new OffsetAndMetadata(r.offset() + 1));
						kafkaProducer.send(new ProducerRecord<>("topic02", r.value() + " consumer send"));
						kafkaProducer.flush();
					});
					// 提交事务
					kafkaProducer.sendOffsetsToTransaction(offsets, "transaction_group");
					kafkaProducer.commitTransaction();
				} catch (Exception exception) {
					log.error("系统错误", exception);
					// 终止事务
					kafkaProducer.abortTransaction();
				}
			}
		}
	}

	/**
	 * 多线程消费者
	 */
	@Test
	public void testThreadConsumer() throws InterruptedException {
		final ConsumerExecutor consumerExecutor = new ConsumerExecutor("k8s-180:9092", "thread-group", "topic01");
		consumerExecutor.execute(2);
		Thread.sleep(10000);
		consumerExecutor.shutdown();
	}

	/**
	 * 消费者线程池，Kafka 中的消费者类是线程不安全的
	 */
	private static class ConsumerExecutor {
		private final KafkaConsumer<String, String> kafkaConsumer;
		private ExecutorService executors;

		public ConsumerExecutor(String brokerList, String groupId, String topic) {
			Properties properties = new Properties();
			properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
			properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
			properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
			this.kafkaConsumer = new KafkaConsumer<>(properties);
			this.kafkaConsumer.subscribe(Collections.singleton(topic));
		}

		public void execute(int workerNum) {
			final ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(workerNum, workerNum * 2, 0L, TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(100),
				ThreadFactoryBuilder.create().setNamePrefix("consumer-thread-").build(),
				new ThreadPoolExecutor.CallerRunsPolicy());
			while (true) {
				final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
				consumerRecords.forEach(r -> poolExecutor.execute(new ConsumerRecordWorker(r)));
			}
		}

		public void shutdown() {
			executors.shutdown();
			kafkaConsumer.close();
		}
	}

	private static class ConsumerRecordWorker implements Runnable {
		private final ConsumerRecord<String, String> record;

		public ConsumerRecordWorker(ConsumerRecord<String, String> record) {
			this.record = record;
		}

		@Override
		public void run() {
			log.info("partition = {}, offset = {}, key =  {}, value = {}", record.partition(), record.offset(), record.key(), record.value());
		}
	}
}
