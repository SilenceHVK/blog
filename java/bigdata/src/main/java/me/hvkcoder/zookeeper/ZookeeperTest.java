package me.hvkcoder.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * @author h_vk
 * @since 2023/6/8
 */
@Slf4j
public class ZookeeperTest {
	private ZooKeeper zooKeeper;

	@Before
	public void connectZookeeper() throws IOException {
		zooKeeper = new ZooKeeper("master.cluster.local:30281", 5000, event -> log.info("zookeeper event => {}", event));
		log.info("zookeeper 连接信息 => {}", zooKeeper);
	}

	@Test
	public void createNode() throws InterruptedException, KeeperException {
		String result = zooKeeper.create("/home", "home".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		log.info("创建节点结果 => {}", result);
	}

	@Test
	public void existsNode() throws InterruptedException, KeeperException {
		Stat stat = zooKeeper.exists("/home/users", false);
		if (stat == null) {
			log.info("节点不存在");
		} else {
			log.info("节点下的子节点个数 => {}", stat.getNumChildren());
		}
	}

	@Test
	public void getChildNode() throws InterruptedException, KeeperException {
		String rootPath = "/home/users";
		zooKeeper.getChildren(rootPath, false).forEach(node -> {
			try {
				String childPath = rootPath + "/" + node;
				byte[] data = zooKeeper.getData(childPath, false, null);
				log.info("{}  =>  {}", childPath, new String(data));
			} catch (KeeperException | InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@Test
	public void setNodeData() throws InterruptedException, KeeperException {
		// -1 表示不指定版本号
		Stat stat = zooKeeper.setData("/home/users", "test".getBytes(), -1);
		if (stat != null) {
			log.info("stat => {}", stat);
		} else {
			log.info("修改失败");
		}
	}

	@Test
	public void deleteNode() throws InterruptedException, KeeperException {
		zooKeeper.delete("/home/users/h_vk", -1);
	}


	@After
	public void close() throws InterruptedException {
		zooKeeper.close();
	}
}
