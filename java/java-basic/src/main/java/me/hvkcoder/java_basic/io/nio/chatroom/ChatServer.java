package me.hvkcoder.java_basic.io.nio.chatroom;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 聊天室服务端
 *
 * @author h_vk
 * @since 2021/12/29
 */
@Slf4j
public class ChatServer {

	public static void main(String[] args) throws IOException {
		new ChatServer(9999).start();
	}

	private final int port;
	private final Selector selector;

	public ChatServer(int port) throws IOException {
		this.port = port;
		this.selector = Selector.open();
	}

	/**
	 * 启动NIO服务端
	 *
	 * @throws IOException
	 */
	public void start() throws IOException {
		try (ServerSocketChannel server = ServerSocketChannel.open()) {
			server.configureBlocking(false);
			server.socket().bind(new InetSocketAddress(port));
			server.register(selector, SelectionKey.OP_ACCEPT);
			log.info("服务端已启动，等待客户端连接.....");

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
		if (selectionKey.isValid() && selectionKey.isAcceptable()) {
			ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = server.accept();
			socketChannel.configureBlocking(false);
			socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
			String message = "客户端 [" + socketChannel.getRemoteAddress() + "] 已上线";
			log.info(message);
			broadcast(message, socketChannel);
		}

		if (selectionKey.isValid() && selectionKey.isReadable()) {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			try {
				ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
				int length = socketChannel.read(buffer);
				String message = "客户端 [" + socketChannel.getRemoteAddress() + "]：" + new String(buffer.array(), 0, length);
				log.info(message);
				broadcast(message, socketChannel);
				buffer.clear();
			} catch (Exception ex) {
				String message = "客户端 [" + socketChannel.getRemoteAddress() + "] 已下线";
				log.info(message);
				selectionKey.cancel();
				socketChannel.close();
				broadcast(message, socketChannel);
			}
		}
	}

	/**
	 * 对接收到的信息进行广播
	 *
	 * @param message
	 * @param self
	 */
	private void broadcast(String message, SocketChannel self) throws IOException {
		for (SelectionKey selectionKey : selector.keys()) {
			SelectableChannel targetChannel = selectionKey.channel();
			// 排除当前的 SocketChannel
			if (targetChannel instanceof SocketChannel && targetChannel != self) {
				((SocketChannel) targetChannel).write(ByteBuffer.wrap(message.getBytes()));
			}
		}
	}
}
