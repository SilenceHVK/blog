package me.hvkcoder.message_queue.kafka.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Map;
import java.util.Properties;

/**
 * 自定义过滤器
 *
 * @author h_vk
 * @since 2022/2/25
 */
@Slf4j
public class CustomPartitioner implements Partitioner {
  public static void main(String[] args) {
    Properties properties = new Properties();
    properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
    properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    // 设置 Partition 分区器
    properties.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, CustomPartitioner.class);

    try (KafkaProducer<String, String> producer = new KafkaProducer<>(properties)) {

      producer.send(
          new ProducerRecord<>("test", "Hello H_VK"),
          (metadata, exception) ->
              log.info("Hello H_VK => partition: {}, offset: {}", metadata.partition(), metadata.offset()));

      producer.send(
          new ProducerRecord<>("test", "Hello"),
          (metadata, exception) ->
              log.info("Hello => partition: {}, offset: {}", metadata.partition(), metadata.offset()));
    }
  }

  /**
   * 分区处理
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
  public int partition(
      String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
    String msgValue = value.toString();
    if (msgValue.contains("H_VK")) {
      return 0;
    }
    return 1;
  }

  @Override
  public void close() {}

  @Override
  public void configure(Map<String, ?> configs) {}
}
