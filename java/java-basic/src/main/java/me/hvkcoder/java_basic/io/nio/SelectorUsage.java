package me.hvkcoder.java_basic.io.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 选择器的本质就是实现 NIO 中的 IO 多路复用， 通常一个线程处理一个选择器，一个选择器监控多个通道
 *
 * @author h_vk
 * @since 2021/7/1
 */
@Slf4j
public class SelectorUsage {
  @Test
  public void testSelectorServer() throws IOException {
    // 创建一个 选择器
    final Selector selector = Selector.open();
    // 创建 ServerSocketChannel
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      // 绑定连接
      serverSocketChannel.socket().bind(new InetSocketAddress(9999));
      // 设置为非阻塞模式
      serverSocketChannel.configureBlocking(false);
      // 将 ServerSocketChannel 注册到 选择器中，并监控 接收 IO 事件
      serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
      log.info("服务器启动成功，等待客户端连接。。。。。。");

      // 轮询 IO 就绪事件，select() 方法阻塞调用，直到至少有一个通道发生了 IO就绪事件
      while (selector.select() > 0) {
        // 获取选择键集合
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
        while (iterator.hasNext()) {
          // 获取 IO就绪事件 Key
          SelectionKey selectionKey = iterator.next();
          // 判断选择键类型
          if (selectionKey.isAcceptable()) {
            // 获取已连接的客户端
            SocketChannel socketChannel = serverSocketChannel.accept();
            log.info("客户端 [{}] 已连接", socketChannel.getRemoteAddress());
            // 将客户端设置为非阻塞模式
            socketChannel.configureBlocking(false);
            // 将客户端通道注册到选择器上，并监控 可写 IO 事件
            socketChannel.register(selector, SelectionKey.OP_WRITE);
          } else if (selectionKey.isWritable()) {
            // 读取客户端通道数据
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            byteBuffer.put("Welcome".getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();

            // 判断客户端是否已断开连接
            if (socketChannel.finishConnect()) {
              log.info("客户端 [{}] 已断开", socketChannel.getRemoteAddress());
              socketChannel.close();
            }
          }
          // 移出选择键，防止重复处理
          iterator.remove();
        }
      }
    }
  }
}
