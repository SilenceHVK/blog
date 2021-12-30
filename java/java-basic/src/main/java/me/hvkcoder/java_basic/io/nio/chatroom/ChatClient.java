package me.hvkcoder.java_basic.io.nio.chatroom;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 聊天室客户端
 *
 * @author h_vk
 * @since 2021/12/29
 */
@Slf4j
public class ChatClient {
	public static void main(String[] args) throws IOException {
		new ChatClient("127.0.0.1", 9999).start();
	}

	private final String host;
	private final int port;
	private final Selector selector;

	public ChatClient(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		this.selector = Selector.open();
	}

	/**
	 * 启动聊天室客户端
	 *
	 * @throws IOException
	 */
	public void start() throws IOException {
		try (SocketChannel socketChannel = SocketChannel.open()) {
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(host, port));
			socketChannel.register(selector, SelectionKey.OP_CONNECT);

			while (selector.select() > 0) {
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while (keyIterator.hasNext()) {
					handle(keyIterator.next());
					keyIterator.remove();
				}
			}
		}
	}

	/**
	 * 处理IO事件
	 *
	 * @param selectionKey
	 */
	private void handle(SelectionKey selectionKey) throws IOException {
		if (selectionKey.isValid() && selectionKey.isConnectable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));

			// 判断连接是否处于就绪状态
			if (socketChannel.isConnectionPending()) {
				socketChannel.finishConnect();  // 完成连接通道
				new Thread(() -> {
					try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
						while (true) {
							String message = bufferedReader.readLine();
							if (message.equals("exit")) {
								selectionKey.cancel();
								socketChannel.close();
								break;
							}
							socketChannel.write(ByteBuffer.wrap(message.getBytes()));

						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
			}
		}

		if (selectionKey.isValid() && selectionKey.isReadable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			try {
				ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
				int length = socketChannel.read(buffer);
				log.info(new String(buffer.array(), 0, length));
				buffer.clear();
			} catch (Exception ex) {
				selectionKey.cancel();
				socketChannel.close();
			}
		}
	}
}
