package me.hvkcoder.message_queue.kafka.serializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Properties;

/**
 * 自定义反序列化类
 *
 * @author h_vk
 * @since 2021/8/5
 */
@Slf4j
public class CustomDeserializer implements Deserializer<Object> {
	@Override
	public Object deserialize(String topic, byte[] data) {
		return SerializationUtils.deserialize(data);
	}

	public static void main(String[] args) {
		final Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CustomDeserializer.class.getName());
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "group_01");

		final KafkaConsumer<String, User> kafkaConsumer = new KafkaConsumer<>(properties);
		kafkaConsumer.subscribe(Collections.singletonList("topic02"));

		try {
			while (true) {
				for (ConsumerRecord<String, User> userConsumerRecord : kafkaConsumer.poll(Duration.ofSeconds(1))) {
					final User user = userConsumerRecord.value();
					log.info("id = {}, name = {}", user.getId(), user.getName());
				}
			}
		} finally {
			kafkaConsumer.close();
		}
	}
}
