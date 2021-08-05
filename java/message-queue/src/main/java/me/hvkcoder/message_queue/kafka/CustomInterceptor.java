package me.hvkcoder.message_queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;

/**
 * 自定义拦截器
 *
 * @author h_vk
 * @since 2021/8/5
 */
@Slf4j
public class CustomInterceptor implements ProducerInterceptor {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, CustomInterceptor.class.getName());

		final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		kafkaProducer.send(new ProducerRecord<>("topic01", "Hello"));
		kafkaProducer.close();
	}

	@Override
	public ProducerRecord onSend(ProducerRecord record) {
		return new ProducerRecord(record.topic(), record.key(), record.value() + "-- kafka");
	}

	@Override
	public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
		log.info("metadata = {} , exception = {}", metadata, exception);
	}

	@Override
	public void close() {

	}

	@Override
	public void configure(Map<String, ?> configs) {

	}
}
