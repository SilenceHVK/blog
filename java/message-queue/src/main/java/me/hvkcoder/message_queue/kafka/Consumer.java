package me.hvkcoder.message_queue.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

/**
 * Kafka 消费者实例
 *
 * @author h-vk
 * @since 2020-03-26
 */
@Slf4j
public class Consumer {
	public static void main(String[] args) {
		// 设置 Consumer 属性
		final Properties properties = new Properties();
		// Kafka 服务地址
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		// Key 反序列化类
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// Value 反序列化类
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		// Consumer 组名称
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer_group1");


		// 消费者首次连接 Kafka offset 策略：latest(默认，最新的一条)、earliest（最开始）、none（未找到消费组的offset，则抛出异常）
//		properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		// 设置消费者自动提交 offset
//		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
		// 设置消费者自动提交 offset 提交间隔
//		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 5000);


		// 创建 Consumer
		final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
		// 订阅主题，可以使用 Pattern.compile("") 正则表达式
		kafkaConsumer.subscribe(Collections.singletonList("topic01"));

		// 手动指定消费分区，该方法将失去组管理特性
//		final List<TopicPartition> topicPartitions = Collections.singletonList(new TopicPartition("topic01", 0));
//		kafkaConsumer.assign(topicPartitions);
		// 设置消费分区的 offset
//		kafkaConsumer.seekToBeginning(topicPartitions);// 从开始位置进行消费
//		kafkaConsumer.seek(new TopicPartition("topic01", 0), 1); // 从指定的 offset 进行消费

		try {
			while (true) {
				// 设置间隔时间获取消息
				final ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofSeconds(1));
				// 记录分区消费元数据信息
				Map<TopicPartition, OffsetAndMetadata> offsets = new HashMap<>();

				consumerRecords.forEach(r -> {
					// 记录分区消费元数据
					offsets.put(new TopicPartition(r.topic(), r.partition()), new OffsetAndMetadata(r.offset()));
					log.info("partition = {}, offset = {}, key =  {}, value = {}", r.partition(), r.offset(), r.key(), r.value());
				});

				// 手动提交给 Kafka 分区元数据
//				kafkaConsumer.commitAsync(offsets, new OffsetCommitCallback() {
//					@Override
//					public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
//						log.info("offsets: {}, exception: {}", offsets, exception);
//					}
//				});
			}
		} finally {
			// 关闭 Consumer
			kafkaConsumer.close();
		}
	}
}
