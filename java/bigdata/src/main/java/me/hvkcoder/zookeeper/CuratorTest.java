package me.hvkcoder.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

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

	@Test
	public void testInterProcessMutex() throws InterruptedException {
		ExecutorService executor = Executors.newFixedThreadPool(10);
		AtomicInteger count = new AtomicInteger();

		// 创建互斥锁
		InterProcessMutex mutex = new InterProcessMutex(curator, "/mutex");
		for (int i = 0; i < 10; i++) {
			executor.execute(() -> {
				try {
					// 获取互斥锁
					mutex.acquire();
					for (int j = 0; j < 10; j++) {
						count.getAndIncrement();
					}
					Thread.sleep(1000);
					log.info("count => {}", count);
				} catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					try {
						// 释放互斥锁
						mutex.release();
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
		}
		Thread.sleep(Integer.MAX_VALUE);
	}

	@After
	public void close() {
		curator.close();
	}

}
