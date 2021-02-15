package me.hvkcoder.java_basic.io.socket.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 基于 TCP 协议的 Socket 通信（客户端）
 *
 * @author h-vk
 * @since 2021/1/15
 */
@Slf4j
public class TcpClient {
	public static void main(String[] args) throws IOException {
		// 创建 socket 实例
		Socket socket = new Socket();
		// 设置 超时时间（秒）
		socket.setSoTimeout(3000);
		// 建立连接, 并设置连接超时时间
		socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), 9090), 3000);

		log.info("客户端：{}:{}", socket.getInetAddress(), socket.getLocalPort());
		log.info("服务端：{}:{}", Inet4Address.getLocalHost(), socket.getPort());

		// 构建键盘输入流
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		// 获取 Socket 输出流
		PrintStream socketPrintStream = new PrintStream(socket.getOutputStream());
		// 获取 Socket 输入流
		BufferedReader socketBufferedReader =
			new BufferedReader(new InputStreamReader(socket.getInputStream()));

		boolean flag = true;
		do {
			// 读取键盘输入消息，并将消息发送给服务器
			String content = input.readLine();
			socketPrintStream.println(content);

			String echo = socketBufferedReader.readLine();
			if ("bye".equalsIgnoreCase(echo)) {
				flag = false;
			}

			log.info("服务端: {}", echo);
		} while (flag);

		// 关闭客户端
		socketBufferedReader.close();
		socketPrintStream.close();
		input.close();
		socket.close();
	}
}
