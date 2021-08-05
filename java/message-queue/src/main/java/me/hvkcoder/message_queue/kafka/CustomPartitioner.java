package me.hvkcoder.message_queue.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义 Kafka 分区策略
 *
 * @author h_vk
 * @since 2021/8/5
 */
@Slf4j
public class CustomPartitioner implements Partitioner {

	private AtomicInteger count = new AtomicInteger(0);


	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class.getName());

		final KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);
		for (int i = 0; i < 6; i++) {
			kafkaProducer.send(new ProducerRecord<>("topic01", "Key-" + i, "Value-" + i));
		}
		kafkaProducer.close();
	}

	/**
	 * 返回消息分区
	 *
	 * @param topic
	 * @param key
	 * @param keyBytes
	 * @param value
	 * @param valueBytes
	 * @param cluster
	 * @return
	 */
	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		// 获取 topic 下所有的 partition
		List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
		int numPartitions = partitionInfos.size();

		// 如果没有指定 Key，则采用 轮询的方式
		if (keyBytes == null) {
			return (count.getAndIncrement() & Integer.MAX_VALUE) % numPartitions;
		}

		// 否则采用 key 值取模的方式
		return Utils.toPositive(Utils.murmur2(keyBytes)) % numPartitions;
	}

	/**
	 * 关闭回调事件
	 */
	@Override
	public void close() {
		log.info("close");
	}

	/**
	 * 获取配置
	 *
	 * @param configs
	 */
	@Override
	public void configure(Map<String, ?> configs) {
		log.info("configs");
	}
}
