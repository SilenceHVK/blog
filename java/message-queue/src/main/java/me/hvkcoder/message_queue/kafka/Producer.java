package me.hvkcoder.message_queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Test;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Kafka 消息生产者实例
 *
 * @author h-vk
 * @since 2020-03-26
 */
@Slf4j
public class Producer {
	static Properties properties;
	static KafkaProducer<String, String> kafkaProducer;

	static {
		// 设置 Producer 属性
		properties = new Properties();
		// Kafka 服务地址
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		// 设置 Key 序列化类
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		// 设置 Value 序列化类
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		kafkaProducer = new KafkaProducer<>(properties);
	}

	@After
	public void closeProducer() {
		// 关闭 producer
		kafkaProducer.close();
	}

	/**
	 * 异步发送消息
	 */
	@Test
	public void testAsyncProducer() {
		kafkaProducer.send(new ProducerRecord<>("topic01", "Hello World"), new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				if (exception == null) {
					log.info("发送消息成功~");
				} else {
					log.error("发送消息失败~", exception);
				}
			}
		});
	}

	/**
	 * 同步发送消息
	 */
	@Test
	public void testSyncProducer() throws ExecutionException, InterruptedException {
		final RecordMetadata recordMetadata = kafkaProducer.send(new ProducerRecord<>("topic01", "Hello World")).get();
		log.info("{}", recordMetadata);
	}

	/**
	 * 无需知道发送结果
	 */
	@Test
	public void testFireAndForgetProducer() {
		for (int i = 0; i < 30; i++) {
			kafkaProducer.send(new ProducerRecord<>("topic01", "Key-" + i, "Value-" + i));
		}
	}

	/**
	 * Kafka 幂等性
	 */
	@Test
	public void testIdempotenceProducer() {
		// 设置 Kafka Ack
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		// 设置确认超时时间
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1);
		// 设置重试次数
		properties.put(ProducerConfig.RETRIES_CONFIG, 3);
		// 开启 Kafka 幂等
		properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		kafkaProducer = new KafkaProducer<>(properties);
		kafkaProducer.send(new ProducerRecord<>("topic01", "Idempotence"));
	}

	/**
	 * 生产者事务
	 */
	@Test
	public void testTransactionProducer() {
		// 开启事务
		properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transactional-id-" + UUID.randomUUID().toString());
		// 设置批量数据大小
		properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1024);
		// 延迟发送等待时间，如果 batch 中的数据不足设置的大小
		properties.put(ProducerConfig.LINGER_MS_CONFIG, 5);
		// 开启事务后，必须开启 幂等、acks=all
		properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 20);

		kafkaProducer = new KafkaProducer<>(properties);

		// 初始化事务
		kafkaProducer.initTransactions();
		try {
			// 开启事务
			kafkaProducer.beginTransaction();
			for (int i = 0; i < 30; i++) {
				kafkaProducer.send(new ProducerRecord<>("topic01", "Key-" + i, "Value-" + i));
				// 将批处理中的数据刷入磁盘
				kafkaProducer.flush();
			}
			// 提交事务
			kafkaProducer.commitTransaction();
		} catch (Exception ex) {
			log.error("发送错误", ex);
			// 终止事务
			kafkaProducer.abortTransaction();
		}
	}
}
