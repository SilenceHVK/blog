package me.hvkcoder.java_basic.io.socket.tcp;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于 TCP 协议的 Socket 通信（服务端）
 *
 * @author h-vk
 * @since 2021/1/15
 */
@Slf4j
public class TcpServer {
	public static void main(String[] args) throws IOException {
		// 创建服务端 Socket 并设置端口号
		ServerSocket server = new ServerSocket(9090);
		log.info("服务端已启动，等待连接.....");

		// 创建线程池
		ThreadPoolExecutor threadPoolExecutor =
			new ThreadPoolExecutor(
				Runtime.getRuntime().availableProcessors(),
				50,
				60,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<>(4),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.CallerRunsPolicy());

		while (true) {
			// 等待客户端连接（阻塞）
			Socket socket = server.accept();
			threadPoolExecutor.execute(
				() -> {
					log.info("新客户端连接：{}:{}", socket.getInetAddress(), socket.getPort());

					PrintStream printStream = null;
					BufferedReader bufferedReader = null;

					try {
						boolean flag = true;
						// 获取输出流
						printStream = new PrintStream(socket.getOutputStream());
						// 获取输入流
						bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						do {
							String str = bufferedReader.readLine();
							if ("bye".equalsIgnoreCase(str)) {
								flag = false;
								printStream.println("bye");
							} else {
								log.info("客户端 {} : {}", socket.getInetAddress(), str);
								printStream.println("字符串长度: " + str.length());
							}
						} while (flag);
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						// 释放资源
						try {
							assert bufferedReader != null;
							bufferedReader.close();
							printStream.close();
							socket.close();
						} catch (IOException exception) {
							exception.printStackTrace();
						}
					}
					log.info("客户端 {} : {}", socket.getInetAddress(), "已断开");
				});
		}
	}
}
