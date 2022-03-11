package me.hvkcoder.java_basic.io.reactor.single;

import io.netty.buffer.ByteBuf;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * @author h_vk
 * @since 2021/7/7
 */
public class ReactorSingleClient {
  public static void main(String[] args) throws IOException {
    new ReactorSingleClient().connect("localhost", 9999);
  }

  private volatile boolean running = false;
  private Selector selector;

  public ReactorSingleClient() throws IOException {
    this.selector = Selector.open();
    this.running = true;
  }

  public void connect(String host, int port) throws IOException {
    try (SocketChannel socketChannel = SocketChannel.open()) {
      socketChannel.configureBlocking(false);
      socketChannel.connect(new InetSocketAddress(host, port));
      socketChannel.register(selector, SelectionKey.OP_CONNECT, ByteBuffer.allocate(1024));
      while (running) {
        if (selector.select() <= 0) continue;
        Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
        while (keyIterator.hasNext()) {
          handle(keyIterator.next());
          keyIterator.remove();
        }
      }
    }
  }

  private void handle(SelectionKey selectionKey) throws IOException {
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
    if (selectionKey.isValid() && selectionKey.isConnectable()) {
      if (socketChannel.isConnectionPending()) {
        socketChannel.finishConnect();
        new Thread(() -> {
					try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
						while(true){
							String message = reader.readLine();
							if ("bye".equals(message)){
								selectionKey.cancel();
								socketChannel.close();
								this.running  = false;
								break;
							}
							socketChannel.write(ByteBuffer.wrap(message.getBytes()));
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}).start();
      }
      selectionKey.interestOps(SelectionKey.OP_READ);
    }

    if (selectionKey.isValid() && selectionKey.isReadable()) {
			try{
				ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
				int length = socketChannel.read(buffer);
        System.out.println("服务端 => "+ new String(buffer.array(), 0, length));
				buffer.clear();
			}catch (Exception exception){
				selectionKey.cancel();
				socketChannel.close();
			}
		}
  }
}
