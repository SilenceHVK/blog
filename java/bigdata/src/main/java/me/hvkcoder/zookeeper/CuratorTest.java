package me.hvkcoder.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author h_vk
 * @since 2023/6/19
 */
@Slf4j
public class CuratorTest {
	private CuratorFramework curator;

	@Before
	public void connectZookeeper() {
		ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(1000, 3);
		curator = CuratorFrameworkFactory.newClient("master.cluster.local:30281", retryPolicy);
		curator.start();
		log.info("zookeeper 连接信息 => {}", curator);
	}

	@Test
	public void createNode() throws Exception {
		String result = curator.create().withMode(CreateMode.PERSISTENT).forPath("/home", "home".getBytes());
		log.info("创建节点结果 => {}", result);
	}

	@Test
	public void existsNode() throws Exception {
		String path = "/home/h_vk";
		Stat stat = curator.checkExists().forPath(path);
		if (stat == null) {
			String result = curator.create().withMode(CreateMode.PERSISTENT).forPath(path, "h_vk".getBytes());
			log.info("不存在，创建 => {}", result);
		} else {
			byte[] bytes = curator.getData().forPath(path);
			log.info("{}", new String(bytes));
		}
	}

	@Test
	public void getChildren() throws Exception {
		curator.getChildren().forPath("/home").forEach(node -> log.info("{}", node));
	}

	@Test
	public void setData() throws Exception {
		Stat stat = curator.setData().forPath("/home/h_vk", "test".getBytes());
		log.info("{}", stat);
	}

	@After
	public void close() {
		curator.close();
	}
	
}
