package me.hvkcoder.spring.practice.kafka;

import lombok.Data;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author h_vk
 * @since 2021/7/28
 */
@Data
@Configuration
public class KafkaConfig {
	// ================ Master Kafka
	@Value("${spring.kafka.master.bootstrap-servers}")
	private String masterBootstrapServers;

	@Value("${spring.kafka.master.consumer.group-id}")
	private String masterConsumerGroupId;

	@Value("${spring.kafka.master.consumer.enable-auto-commit}")
	private boolean masterConsumerEnableAutoCommit;

	// ================ Slave Kafka
	@Value("${spring.kafka.slave.bootstrap-servers}")
	private String slaveBootstrapServers;

	@Value("${spring.kafka.slave.consumer.group-id}")
	private String slaveConsumerGroupId;

	@Value("${spring.kafka.slave.consumer.enable-auto-commit}")
	private boolean slaveConsumerEnableAutoCommit;

	@Bean
	public KafkaTemplate<String, String> masterKafkaTemplate() {
		return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigs(masterBootstrapServers)));
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> masterContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		final Map<String, Object> consumerConfigs = consumerConfigs(masterBootstrapServers, masterConsumerGroupId, masterConsumerEnableAutoCommit);
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerConfigs));
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}

	@Bean
	public KafkaTemplate<String, String> slaveKafkaTemplate() {
		return new KafkaTemplate<>(
			new DefaultKafkaProducerFactory<>(producerConfigs(slaveBootstrapServers)));
	}

	@Bean
	KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<Integer, String>> slaveContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerConfigs(slaveBootstrapServers, slaveConsumerGroupId, slaveConsumerEnableAutoCommit)));
		factory.setConcurrency(3);
		factory.getContainerProperties().setPollTimeout(3000);
		return factory;
	}

	/**
	 * 生产者配置
	 *
	 * @param bootstrapServers
	 * @return
	 */
	private Map<String, Object> producerConfigs(String bootstrapServers) {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.RETRIES_CONFIG, 0);
		props.put(ProducerConfig.ACKS_CONFIG, "1");
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}

	/**
	 * 消费者配置
	 *
	 * @param bootstrapServers
	 * @param groupId
	 * @param enableAutoCommit
	 * @return
	 */
	private Map<String, Object> consumerConfigs(String bootstrapServers, String groupId, boolean enableAutoCommit) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, enableAutoCommit);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return props;
	}
}
