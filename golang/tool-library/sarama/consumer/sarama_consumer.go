package main

import (
	"fmt"
	"github.com/Shopify/sarama"
	"sync"
)

func main() {
	// 1. 连接 Kafka 服务端
	consumer, err := sarama.NewConsumer([]string{"node-1:9092"}, nil)
	defer consumer.Close()
	if err != nil {
		fmt.Println("连接 Kafka 服务端失败， err: ", err)
		return
	}

	// 2. 拉取 topic 下所有 partition 数据
	const TOPIC = "test"
	partitions, err := consumer.Partitions(TOPIC)
	if err != nil {
		fmt.Println("拉取 Partition 数据失败， err: ", err)
		return
	}

	// 3. 循环遍历 partition
	var wg sync.WaitGroup
	wg.Add(1)
	for partition := range partitions {
		// 为每个 partition 创建对应的消费者
		consumePartition, err := consumer.ConsumePartition(TOPIC, int32(partition), sarama.OffsetNewest)
		defer consumePartition.AsyncClose()
		if err != nil {
			fmt.Printf("获取 Partition: %v 数据失败， err: %v \n", partition, err)
			return
		}

		// 开启协程异步消费
		go func(partitionConsumer sarama.PartitionConsumer) {
			defer wg.Done()
			for message := range partitionConsumer.Messages() {
				fmt.Printf("Partition: %v， Offset: %v， Message: %v \n", message.Partition, message.Offset, string(message.Value))
			}
		}(consumePartition)
	}
	wg.Wait()
}
