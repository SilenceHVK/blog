package me.hvkcoder.java_basic.io.reactor.master_slave;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author h_vk
 * @since 2022/3/15
 */
public class ReactorMasterSlave {
  public static void main(String[] args) throws IOException {
    new MainReactor(Selector.open()).listen(9999);
  }

  private record MainReactor(Selector selector) {
		public void listen(int port) throws IOException {
			try(ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()){
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.socket().bind(new InetSocketAddress(port));
				SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				selectionKey.attach(new Acceptor(
					serverSocketChannel,
					Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
					));
				System.out.println("服务端已启动，等待客户端连接......");
				while(true){
					if (selector.selectNow() <= 0) continue;
					Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
					while(selectionKeyIterator.hasNext()){
						dispatch(selectionKeyIterator.next());
						selectionKeyIterator.remove();
					}
				}
			}
		}

		private void dispatch(SelectionKey selectionKey){
			Object attachment = selectionKey.attachment();
			if (attachment instanceof  Runnable){
				((Runnable) attachment).run();
			}
		}
	}

	private record  Acceptor(ServerSocketChannel serverSocketChannel, ExecutorService service)  implements Runnable{
		@SneakyThrows
		@Override
		public void run() {
			SocketChannel socketChannel = serverSocketChannel.accept();
			if (socketChannel != null){
				System.out.println("【 客户端 "+ socketChannel.getRemoteAddress()+" 】已连接");
				SubReactor subReactor = new SubReactor(Selector.open());
				subReactor.register(socketChannel);
				service.execute(subReactor);
			}
		}
	}

	private record SubReactor(Selector selector) implements Runnable {
		public void register(SocketChannel socketChannel) throws IOException {
			socketChannel.configureBlocking(false);
			SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
			selectionKey.attach(new IOHandle(selectionKey, ByteBuffer.allocate(1024), Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())));
		}

		@SneakyThrows
		@Override
		public void run() {
			while(!Thread.interrupted()){
				if (selector.selectNow() <= 0) continue;
				Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
				while(selectionKeyIterator.hasNext()){
					dispatch(selectionKeyIterator.next());
					selectionKeyIterator.remove();
				}
			}
		}

		private void dispatch(SelectionKey selectionKey){
			Object attachment = selectionKey.attachment();
			if (attachment instanceof Runnable){
				((Runnable) attachment).run();
			}
		}
	}

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
					String result = worker.submit(() -> {
						try {
							TimeUnit.SECONDS.sleep(5);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						return message + "-" + Thread.currentThread().getName();
					}).get();
					socketChannel.write(ByteBuffer.wrap(result.getBytes()));
				}
			}catch (Exception exception){
				System.out.println("【 客户端 "+ socketChannel.getRemoteAddress()+" 】已离线");
				socketChannel.close();
				selectionKey.cancel();
			}
		}
	}

}
