package me.hvkcoder.java_basic.io.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
}
