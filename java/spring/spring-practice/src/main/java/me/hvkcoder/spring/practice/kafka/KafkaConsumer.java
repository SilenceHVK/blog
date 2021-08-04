package me.hvkcoder.spring.practice.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author h_vk
 * @since 2021/7/28
 */
@Slf4j
@Component
public class KafkaConsumer {
	@KafkaListener(topics = {"test"}, containerFactory = "masterContainerFactory")
	public void masterConsumer(ConsumerRecord<?,?> record){
		log.info("master: {}",record.value());
	}

  //	@KafkaListener(topics = {"test"}, containerFactory = "slaveContainerFactory")
  //	public void slaveConsumer(ConsumerRecord<?,?> record){
  //		log.info("slave: {}",record.value());
  //	}
}
