package me.hvkcoder.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;

/**
 * Zookeeper Kerberos 认证
 *
 * @author h_vk
 * @since 2022/5/19
 */
public class ZookeeperByKerberos {
	private static String connectString = "node-1:2181";
	private static int sessionTimeout = 200 * 1000;

	public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
		// 设置 Kerberos 认证文件位置
		System.setProperty("java.security.auth.login.config", ClassLoader.getSystemResource("kerberos/zk-cli-jaas.conf").getPath());
		System.setProperty("java.security.krb5.conf", ClassLoader.getSystemResource("kerberos/krb5.conf").getPath());

		ZooKeeper zooKeeper = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
			@Override
			public void process(WatchedEvent event) {

			}
		});

		byte[] data = zooKeeper.getData("/test", false, null);
		System.out.println(new String(data));
		zooKeeper.close();
	}
}
