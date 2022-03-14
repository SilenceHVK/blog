package me.hvkcoder.java_basic.io.reactor.multi;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author h_vk
 * @describe
 * @since 2022/3/11
 */
public class ReactorMultiThread {
	public static void main(String[] args) throws IOException {
		new ReactorMultiThread().listen(9999);
	}

	private volatile boolean running = false;
	private final Selector selector;

	public ReactorMultiThread() throws IOException {
		this.running = true;
		this.selector = Selector.open();
	}

	/**
	 * 开启服务端监听
	 *
	 * @param port
	 * @throws IOException
	 */
	private void listen(int port) throws IOException {
		try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new Acceptor(serverSocketChannel, selector));
			System.out.println("服务端已运行，等待客户端连接......");
			while (running) {
				if (selector.select() <= 0) continue;
				Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
				while (keyIterator.hasNext()) {
					dispatch(keyIterator.next());
					keyIterator.remove();
				}
			}
		}
	}

	/**
	 * 派发 IO 事件方法
	 *
	 * @param selectionKey
	 */
	private void dispatch(SelectionKey selectionKey) {
		Object attachment = selectionKey.attachment();
		if (attachment instanceof Runnable) {
			((Runnable) attachment).run();
		}
	}

	/**
	 * 处理 IO 连接事件
	 */
	private record Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) implements Runnable {
		@SneakyThrows
		@Override
		public void run() {
			SocketChannel socketChannel = serverSocketChannel.accept();
			if (socketChannel != null){
				System.out.println("【 客户端 "+ socketChannel.getRemoteAddress()+" 】已连接");
				socketChannel.configureBlocking(false);
				SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
				selectionKey.attach(
						new IOHandle(selectionKey,
						ByteBuffer.allocate(1024),
						Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()) //  增加 worker 线程池处理具体的业务
				));
				selector.wakeup();
			}
		}
	}

	/**
	 * 处理其他 IO 事件
	 */
	private record IOHandle(SelectionKey selectionKey, ByteBuffer buffer, ExecutorService worker) implements Runnable{

		@SneakyThrows
		@Override
		public void run() {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			try{
				if (selectionKey.isValid() && selectionKey.isReadable()){
					int length = socketChannel.read(buffer);
					String message = new String(buffer.array(), 0, length);
					System.out.println("【客户端 " + socketChannel.getRemoteAddress() + "】=> " + message);
					buffer.clear();
					// 开启线程处理业务数据
					String result = worker.submit(() -> {
						try {
							TimeUnit.SECONDS.sleep(3);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return message + "-" + Thread.currentThread().getName();
					}).get();
					socketChannel.write(ByteBuffer.wrap(result.getBytes()));
				}
			}catch(Exception exception){
				System.out.println("【 客户端 "+ socketChannel.getRemoteAddress()+" 】已离线");
				selectionKey.cancel();
				socketChannel.close();
			}
		}
	}
}
