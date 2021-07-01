package me.hvkcoder.java_basic.io.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Channel 用于在字节缓冲区和位于通道另一侧的实体（文件或套接字）之间有效地传输数据
 *
 * <p>Channel 在创建时是打开的，一旦关闭，任何对其调用 I/O 操作都会抛出 ClosedChannelException
 *
 * <p>Channel 的实现类
 *
 * <ul>
 *   <li>FileChannel 主要用于读写文件的通道
 *   <li>SocketChannel
 *   <li>ServerSocketChannel
 *   <li>DatagramChannel
 * </ul>
 *
 * @author h_vk
 * @since 2021/6/12
 */
@Slf4j
public class ChannelUsage {

  /**
   * FileChannel 使用 ByteBuffer 拷贝文件 MappedByteBuffer 是直接内存缓冲区，是通过 FileChannel.map
   * 创建的，将磁盘读写变为内存读写，效率要比直接操作 ByteBuffer 高 拷贝的文件必须小于 2G
   */
  @Test
  public void testFileChannelWithByteBufferFileCopy() {
    try (
    // fileInputStream 为只读，fileOutputStream 只写
    FileInputStream fileInputStream = new FileInputStream("/Users/h_vk/Downloads/pom.xml");
        FileOutputStream fileOutputStream =
            new FileOutputStream("/Users/h_vk/Downloads/pom_copy.xml");

        // 获取 FileChannel
        final FileChannel readChannel = fileInputStream.getChannel();
        final FileChannel writeChannel = fileOutputStream.getChannel(); ) {

      ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
      while (readChannel.read(byteBuffer) != -1) {
        byteBuffer.flip();
        writeChannel.write(byteBuffer);
        byteBuffer.clear();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** ServerSocketChannel 监听通道对应 OIO 中的 ServerSocket */
  @Test
  public void testServerSocketChannel() {
    // 获取一个 ServerSocketChannel
    try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
      // 设置 ServerSocketChannel 绑定端口
      serverSocketChannel.socket().bind(new InetSocketAddress(9999));
      log.info("服务端已启动，等待客户端连接。。。。。。");
      // 设置非阻塞
      serverSocketChannel.configureBlocking(false);
      // 自旋等待客户连接
      while (true) {
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 由于非阻塞的，所以要判断 socketChannel 是否为 null
        if (socketChannel != null) {
          log.info("客户端 [{}] 已连接", socketChannel.getRemoteAddress());

          // 向客户端发送消息
          ByteBuffer buffer = ByteBuffer.allocate(1024);
          buffer.put("Welcome".getBytes());
          buffer.flip();
          socketChannel.write(buffer);
          buffer.clear();

					// 判断客户端是否断开连接
          if (socketChannel.finishConnect()) {
            log.info("客户端 [{}] 已断开", socketChannel.getRemoteAddress());
            socketChannel.close();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** SocketChannel 传输通道对应 OIO 中的 Socket */
  @Test
  public void testSocketChannel() {
    // 获取 SocketChannel，并连接到服务器
    try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(9999))) {
      // 自旋判断是否连接服务器
      while (!socketChannel.finishConnect()) {}
      log.info("已连接服务器");

      // 接收服务器的数据
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      final int dataLength = socketChannel.read(buffer);
      log.info("服务端：{}", new String(buffer.array(), 0, dataLength));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
