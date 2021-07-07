package me.hvkcoder.java_basic.io.reactor.single;

import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author h_vk
 * @since 2021/7/7
 */
public class ReactorSingleServer implements Runnable {

  public static void main(String[] args) throws IOException {
    new Thread(new ReactorSingleServer(9999)).start();
  }

  private final Selector selector;
  private final ServerSocketChannel serverSocketChannel;

  public ReactorSingleServer(int port) throws IOException {
    selector = Selector.open();
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.bind(new InetSocketAddress(port));
    serverSocketChannel.configureBlocking(false);
    final SelectionKey selectionKey =
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    selectionKey.attach(new Acceptor());
    System.out.println("服务器已启动，等待客户端连接。。。。。。");
  }

  private void dispatch(SelectionKey selectionKey) {
    final Runnable attachment = (Runnable) selectionKey.attachment();
    if (attachment != null) {
      attachment.run();
    }
  }

  @SneakyThrows
  @Override
  public void run() {
    while (!Thread.interrupted()) {
      if (selector.select() > 0) {
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          dispatch(iterator.next());
        }
        iterator.remove();
      }
    }
  }

  private class Acceptor implements Runnable {
    @SneakyThrows
    @Override
    public void run() {
      final SocketChannel socketChannel = serverSocketChannel.accept();
      if (socketChannel != null) {
        System.out.println("客户端 [" + socketChannel.getRemoteAddress() + "] 已连接");
        new IOHandler(socketChannel);
      }
    }
  }

  private class IOHandler implements Runnable {
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);
    private final SocketChannel socketChannel;
    private final SelectionKey selectionKey;

    public IOHandler(SocketChannel socketChannel) throws IOException {
      this.socketChannel = socketChannel;
      this.socketChannel.configureBlocking(false);
      selectionKey =
          this.socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      selectionKey.attach(this);
      selector.wakeup();
    }

    @Override
    public void run() {
      try {
        if (selectionKey.isValid() && selectionKey.isReadable()) {
          int dataLength = 0;
          while ((dataLength = socketChannel.read(buffer)) > 0) {
            System.out.println(
                "客户端["
                    + socketChannel.getRemoteAddress()
                    + "]："
                    + new String(buffer.array(), 0, dataLength));
            buffer.clear();
          }
          selectionKey.interestOps(SelectionKey.OP_WRITE);
        }

        if (selectionKey.isValid() && selectionKey.isWritable()) {
          buffer.put("Welcome".getBytes());
          buffer.flip();
          socketChannel.write(buffer);
          buffer.clear();
          selectionKey.interestOps(SelectionKey.OP_READ);
        }
      } catch (IOException e) {
        try {
          System.out.println("客户端[" + socketChannel.getRemoteAddress() + "] 已断开连接");
          selectionKey.cancel();
          socketChannel.close();
        } catch (IOException ioException) {
          ioException.printStackTrace();
        }
      }
    }
  }
}
