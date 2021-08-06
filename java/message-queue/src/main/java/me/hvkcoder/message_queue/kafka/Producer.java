package me.hvkcoder.message_queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.After;
import org.junit.Test;

import java.util.Properties;
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

		// 设置 Kafka Ack
		properties.put(ProducerConfig.ACKS_CONFIG, "all");
		// 设置确认超时时间
		properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 1);
		// 设置重试次数
		properties.put(ProducerConfig.RETRIES_CONFIG, 3);
		// 开启 Kafka 幂等
		properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

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
}
