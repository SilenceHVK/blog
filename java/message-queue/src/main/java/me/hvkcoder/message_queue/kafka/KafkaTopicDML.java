package me.hvkcoder.message_queue.kafka;

import org.apache.kafka.clients.admin.*;
import org.junit.After;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

/**
 * Kafka Topic 管理
 *
 * @author h-vk
 * @since 2020-03-25
 */
public class KafkaTopicDML {
	static Properties properties = new Properties();
	static AdminClient adminClient = null;

	static {
		/**  设置 Kafka 集群地址 */
		properties.setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "k8s-180:9092");
		/**  创建 KafkaAdminClient */
		adminClient = KafkaAdminClient.create(properties);
	}

	@After
	public void closeAdminClient() {
		// 关闭 Kafka 连接
		adminClient.close();
	}

	/**
	 * 创建 Topic
	 */
	@Test
	public void testCreateTopic() throws ExecutionException, InterruptedException {
		final CreateTopicsResult createTopicsResult = adminClient.createTopics(Arrays.asList(
			new NewTopic("topic01", 1, (short) 1),
			new NewTopic("topic02", 1, (short) 1)
		));
		// 同步创建
		createTopicsResult.all().get();
	}

	/**
	 * 获取 Topic 列表
	 */
	@Test
	public void testGetTopicList() throws ExecutionException, InterruptedException {
		final ListTopicsResult listTopicsResult = adminClient.listTopics();
		listTopicsResult.names().get().forEach(System.out::println);
	}

	/**
	 * 获取 Topic 详情
	 */
	@Test
	public void testTopicDescribe() throws ExecutionException, InterruptedException {
		final DescribeTopicsResult describeTopicsResult = adminClient.describeTopics(Collections.singletonList("topic01"));
		describeTopicsResult.all().get().forEach((s, topicDescription) -> System.out.println(s + " : " + topicDescription));
	}

	/**
	 * 删除 Topic
	 */
	@Test
	public void testDeleteTopic() throws ExecutionException, InterruptedException {
		adminClient.deleteTopics(Collections.singletonList("topic02")).all().get();
	}
}
