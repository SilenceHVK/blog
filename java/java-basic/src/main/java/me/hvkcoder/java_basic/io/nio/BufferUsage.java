package me.hvkcoder.java_basic.io.nio;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Buffer 是一个对象，它包含一些要写入或者要读取的数据，所有的数据都用缓冲区处理。缓冲区实际上是一个数组，
 * 通常它是一个字节数组（ByteBuffer），也可以使用其他类型的数组。这个数组为缓冲区提供了数据的访问读写等操作属性
 *
 * <p>Buffer 每一种类型都对应 Java 的基本类型（Boolean 类型除外）
 *
 * <ul>
 *   <li>ByteBuffer 【最常用】
 *   <li>IntBuffer
 *   <li>ShortBuffer
 *   <li>LongBuffer
 *   <li>FloatBuffer
 *   <li>CharBuffer
 *   <li>DoubleBuffer
 * </ul>
 *
 * @author h_vk
 * @since 2021/6/10
 */
@Slf4j
public class BufferUsage {

  /**
   * ByteBuffer 的创建提供了三种创建方式，以下三种方式适用于 Buffer 的所有 7 中类型
   *
   * <ul>
   *   <li>allocate(int capacity)：在 JVM 的堆中创建指定的缓冲区，这种方式被叫做 间接缓冲区， 创建与销毁缓冲区的效率比较高
   *   <li>allocateDirect(int capacity)：在系统内存中创建缓冲区，这种方式被叫做 直接缓冲区，缓冲区的工作效率交高
   *   <li>wrap(byte[] array, int offset, int length)：使用指定的数组作为缓冲区的存储器
   * </ul>
   */
  @Test
  public void testCreateByteBuffer() {
    final ByteBuffer allocate = ByteBuffer.allocate(10);
    log.info(Arrays.toString(allocate.array()));

    final ByteBuffer allocateDirect = ByteBuffer.allocateDirect(10);
    final ByteBuffer wrap = ByteBuffer.wrap(new byte[10]);
  }

  /**
   * 所有缓冲区都具有四个属性来提供关于其所包含的数据元素的信息
   *
   * <ul>
   *   <li>Capacity（容量）：缓冲区能够容纳的数据元素的最大数量，在缓冲区被创建时设置，且永远不能被修改
   *   <li>Limit（上界）：Buffer 当前不能读或写的位置
   *   <li>Position（位置）：Buffer 当前可读或写的位置，由 get() 或 put() 函数更新
   *   <li>Mark（标记）：标记位置，调用 mark() 方法设置 mark = position；调用 reset() 方法设置 position = mark
   * </ul>
   *
   * 这四个属性遵循：0 <= mark <= position <= limit <= capacity
   */
  @Test
  public void testBufferByProperty() {
    ByteBuffer allocate = ByteBuffer.allocate(10);

    // Buffer 在初始化之后，capacity 和 limit 是相等的， position = 0
    log.info("Buffer 的 容量： {}", allocate.capacity());
    log.info("Buffer 的 上界：{}", allocate.limit());
    log.info("Buffer 当前位置：{}", allocate.position());
    // 标记 position 位置，此时 position = 0
    allocate.mark();

    allocate.put((byte) 10);
    allocate.put((byte) 20);
    allocate.put((byte) 30);

    // 设置 limit
    allocate.limit(5);

    allocate.put((byte) 40);
    allocate.put((byte) 50);
    log.info(Arrays.toString(allocate.array()));
    //  allocate.put((byte) 60); //Buffer 将抛出 java.nio.BufferOverflowException 异常， position 不能大于
    // limit
    log.info("Buffer 当前位置：{}", allocate.position());

    // 设置 position
    allocate.position(2);
    log.info("Buffer 当前位置：{}", allocate.position());

    // 将 position 设置为标记位置
    allocate.reset();
    log.info("Buffer 当前位置：{}", allocate.position());
  }

  /** Buffer 添加元素 */
  @Test
  public void testBufferByPut() {
    ByteBuffer allocate = ByteBuffer.allocate(10);
    allocate.put((byte) 10);

    // 在指定位置插入元素，不会改变 position 的值
    allocate.put(4, (byte) 50);
    log.info(Arrays.toString(allocate.array()));

    // 插入一个 byte 数组
    byte[] bytes = {60, 70, 80};
    allocate.put(bytes);
    log.info(Arrays.toString(allocate.array()));

    // 插入一个指定范围的 byte 数组
    byte[] bytesRange = {90, 100, 101};
    allocate.put(bytesRange, 1, 2);
    log.info(Arrays.toString(allocate.array()));
  }

  /** Buffer 常用 API */
  @Test
  public void testBufferByAPI() {
    ByteBuffer allocate = ByteBuffer.allocate(10);
    allocate.put((byte) 'H');
    allocate.put((byte) 'e');
    allocate.put((byte) 'l');
    allocate.put((byte) 'l');
    allocate.put((byte) 'o');

    log.info("flip 前 limit = {}", allocate.limit());
    log.info("flip 前 position = {}", allocate.position());
    log.info("flip 前 get = {}", allocate.get());

    // flip 函数将填充状态的缓冲区翻转城一个准备读出元素的释放状态
    // limit = position; position = 0 ; mark = -1;
    allocate.flip();
    log.info("flip 后 limit = {}", allocate.limit());
    log.info("flip 后 position = {}", allocate.position());
    log.info("flip 前 get = {}", allocate.get());

    // rewind 函数与 flip 相似，但是不会更改 limit 的值，只是将 position 归置为 0
    // position = 0; mark = -1;
    allocate.rewind();

    log.info("从当前为位置到上界还剩余 {} 个元素", allocate.remaining());

    // hasRemaining 函数用于判断是否已达到缓冲区的上界
    while (allocate.hasRemaining()) {
      log.info("{}", allocate.get());
    }

    // clear 函数将缓冲区重置为空状态，它并不改变缓冲区内的任何元素，只是将 上界 设为 容量值
		// position = 0; limit = capacity; mark = -1;
    allocate.clear();
  }
}
