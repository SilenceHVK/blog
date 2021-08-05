package me.hvkcoder.message_queue.kafka.serializer;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

/**
 * 自定义序列化类
 *
 * @author h_vk
 * @since 2021/8/5
 */
public class CustomSerializer implements Serializer<Object> {
	@Override
	public byte[] serialize(String topic, Object data) {
		return SerializationUtils.serialize((Serializable) data);
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CustomSerializer.class.getName());

		final KafkaProducer<String, User> kafkaProducer = new KafkaProducer<>(properties);
		for (int i = 0; i < 6; i++) {
			kafkaProducer.send(new ProducerRecord<>("topic02", "Key-" + i, new User(i, "user-" + i)));
		}
		kafkaProducer.close();
	}
}
