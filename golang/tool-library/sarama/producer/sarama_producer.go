package main

import (
	"fmt"
	"github.com/Shopify/sarama"
)

// 使用 sarama 向 kafka 发送数据
func main() {
	// 1. 设置 sarama 配置
	config := sarama.NewConfig()
	// 设置 ACK 确认
	config.Producer.RequiredAcks = sarama.WaitForAll
	// 设置 partition 选择方式
	config.Producer.Partitioner = sarama.NewRandomPartitioner
	// 成功发送的消息返回在 Success Channel 中
	config.Producer.Return.Successes = true

	// 2. 构建消息
	message := &sarama.ProducerMessage{
		Topic: "test",
		Value: sarama.StringEncoder("This is test log"),
	}

	// 3. 连接 Kafka 服务端
	producer, err := sarama.NewSyncProducer([]string{"node-1:9092"}, config)
	defer producer.Close()
	if err != nil {
		fmt.Println("连接 Kafka 失败， err: ", err)
		return
	}

	// 4. 发送消息
	partition, offset, err := producer.SendMessage(message)
	if err != nil {
		fmt.Println("发送消息失败， err: ", err)
		return
	}
	fmt.Printf("partition: %v, offset: %v\n", partition, offset)
}
