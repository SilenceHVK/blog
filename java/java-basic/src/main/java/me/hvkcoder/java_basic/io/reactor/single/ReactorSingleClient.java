package me.hvkcoder.java_basic.io.reactor.single;

import java.io.IOException;
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
    final ByteBuffer buffer = ByteBuffer.allocate(1024);
    Selector selector = Selector.open();
    SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(9999));
    socketChannel.configureBlocking(false);
    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    while (!socketChannel.finishConnect()) {}
    System.out.println("已连接服务器");

    while (selector.select() > 0) {
      final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
      while (iterator.hasNext()) {
        final SelectionKey selectionKey = iterator.next();

        if (selectionKey.isValid() && selectionKey.isReadable()) {
          final SocketChannel channel = (SocketChannel) selectionKey.channel();
          if (channel != null) {
            int dataLength = 0;
            while ((dataLength = socketChannel.read(buffer)) > 0) {
              System.out.println(
                  "客户端 ["
                      + socketChannel.getRemoteAddress()
                      + "]："
                      + new String(buffer.array(), 0, dataLength));
              buffer.clear();
            }
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_WRITE);
          }
        }

        if (selectionKey.isValid() && selectionKey.isWritable()) {
          final SocketChannel channel = (SocketChannel) selectionKey.channel();
          if (channel != null) {
            System.out.print("请输入发送内容：");
            final Scanner scanner = new Scanner(System.in);
            if (scanner.hasNext()) {
              buffer.put(scanner.next().getBytes());
              buffer.flip();
              channel.write(buffer);
              buffer.clear();
            }
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
          }
        }

        iterator.remove();
      }
    }
  }
}
