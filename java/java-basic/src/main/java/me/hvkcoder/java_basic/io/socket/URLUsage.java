package me.hvkcoder.java_basic.io.socket;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL（Uniform Resource Locator）统一资源定位符，表示 Internet 上某一资源地址
 *
 * <p>由两部分组成：协议名称和资源名称，中间用冒号隔开
 *
 * @author h-vk
 * @since 2021/1/15
 */
@Slf4j
public class URLUsage {
	/**
	 * 获取 URL 信息
	 *
	 * @throws MalformedURLException
	 */
	@Test
	public void URLInfo() throws MalformedURLException {
		URL blog = new URL("https://blog.hvkcoder.me");

		// 对已存在的 URL 进行二次实例，相当于拼接
		URL terminal = new URL(blog, "/terminal/index.html?name=hvkcoder#test");
		log.info("协议：{}", terminal.getProtocol());
		log.info("主机：{}", terminal.getHost());

		// 相关协议端口号
		log.info("默认端口号：{}", terminal.getDefaultPort());
		log.info("文件路径：{}", terminal.getPath());
		log.info("文件名称：{}", terminal.getFile());
		log.info("相对路径：{}", terminal.getRef());
		log.info("查询字段：{}", terminal.getQuery());
	}

	/**
	 * URL 提供 openStream 方法，用于读取 URL 中的内容
	 */
	@Test
	public void openStream() throws IOException {
		URL blog = new URL("https://blog.hvkcoder.me");
		InputStream inputStream = blog.openStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String data;

		// 循环读取内容
		while ((data = reader.readLine()) != null) {
			log.info(data);
		}
		reader.close();
	}
}
