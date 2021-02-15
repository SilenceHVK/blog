package me.hvkcoder.java_basic.io.socket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * 用于标识网络上的硬件资源，表示互联网协议（IP）地址
 *
 * @author h-vk
 * @since 2021/1/15
 */
@Slf4j
public class InetAddressUsage {

	public static void getAddressInfo(InetAddress address) {
		log.info("主机名称：{} ", address.getHostName());
		log.info("IP：{} ", address.getHostAddress());
		log.info("字节数组形式的IP：{}", Arrays.toString(address.getAddress()));
	}

	/**
	 * 通过主机名或IP地址获取主机信息
	 */
	@Test
	public void getByName() throws UnknownHostException {
		InetAddress address = InetAddress.getByName("localhost");
		getAddressInfo(address);
	}

	/**
	 * 获取主机信息
	 */
	@Test
	public void getLocalHost() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		getAddressInfo(address);
	}
}
