package me.hvkcoder.java_basic.io.socket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * NetWorkInterface 用于提供访问网卡设备的相关信息
 *
 * @author h-vk
 * @since 2021/1/15
 */
@Slf4j
public class NetWorkInterfaceUsage {
	public static void main(String[] args) throws SocketException {
		// 获取 networkInterface 实例
		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();
			log.info("getName 获取网络设备名称 = {}", networkInterface.getName());
			log.info("getDisplayName 获得网络设备显示名称 = {}", networkInterface.getDisplayName());
			log.info("getIndex 获得网络接口的索引 = {}", networkInterface.getIndex());
			log.info("isUp 是否已经开启并运行 = {}", networkInterface.isUp());
			log.info("isLoopback 是否为回调接口 = {}", networkInterface.isLoopback());

			/** MTU（Maximum Transmission Unit，最大传输单元）来规定网络传输最大数据包大小，单位为字节。 */
			log.info("getMTU 获取最大传输单元 = {}", networkInterface.getMTU());
			/**
			 * 单播：单台主机与单台主机之间的通信
			 *
			 * <p>广播：单台主机与网络中所有主机的通信
			 *
			 * <p>组播：单台主机与选定的一组主机的通信
			 */
			log.info("supportsMulticast 是否支持多播 = {}", networkInterface.supportsMulticast());

			log.info(
				"==================================================================================");
		}
	}

	/**
	 * 根据网络设备索引获取 NetWorkInterface 对象
	 *
	 * @throws SocketException
	 */
	@Test
	public void getByIndex() throws SocketException {
		NetworkInterface networkInterface = NetworkInterface.getByIndex(1);
		log.info("getName 获取网络设备名称 = {}", networkInterface.getName());
		log.info("getDisplayName 获得网络设备显示名称 = {}", networkInterface.getDisplayName());
	}

	/**
	 * 根据网络设备名称获取 NetWorkInterface 对象
	 *
	 * @throws SocketException
	 */
	@Test
	public void getByName() throws SocketException {
		NetworkInterface networkInterface = NetworkInterface.getByName("lo0");
		log.info("getName 获取网络设备名称 = {}", networkInterface.getName());
		log.info("getDisplayName 获得网络设备显示名称 = {}", networkInterface.getDisplayName());
	}
}
