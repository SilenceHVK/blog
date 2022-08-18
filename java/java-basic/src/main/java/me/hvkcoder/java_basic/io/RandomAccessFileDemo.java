package me.hvkcoder.java_basic.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * RandomAccessFile 支持随机读写文件，通常使用场景为 多线程下载和断点续传
 * 读写模式为：
 * r    只读的方式打开
 * rw   读写操作
 * rws  执行写操作后，同步刷新到磁盘，刷新内容和元数据
 * rwd  执行写操作后，同步刷新到磁盘，刷新内容
 * <p>
 * 重要方法：
 * long getFilePointer( ); // 获取文件记录指针的当前位置
 * void seek(long pos); // 将文件指针定位 pos 位置
 *
 * @author h_vk
 * @since 2022/8/18
 */
public class RandomAccessFileDemo {
	private final static String filePath = "/Users/h_vk/Downloads/hello.txt";

	/**
	 * 文件随机读写
	 *
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		int pos = 20;
		File tempFile = File.createTempFile("tmp", null);
		tempFile.deleteOnExit(); // remove tempFile on jvm exit

		try (
			RandomAccessFile accessFile = new RandomAccessFile(filePath, "rw");
			RandomAccessFile tempAccessFile = new RandomAccessFile(tempFile, "rw");
		) {
			// 将指针位置后的内容存储在临时文件中
			accessFile.seek(pos);
			int hasIndex = -1;
			byte[] bytes = new byte[1024];
			while ((hasIndex = accessFile.read(bytes)) != -1) {
				tempAccessFile.write(bytes, 0, hasIndex);
			}

			// 在执行的指针位置插入内容
			accessFile.seek(pos);
			accessFile.writeBytes("\nHello Golang\n");

			// 将临时文件中的内容再写入原文件中
			tempAccessFile.seek(0);
			while ((hasIndex = tempAccessFile.read(bytes)) != -1) {
				accessFile.write(bytes, 0, hasIndex);
			}
		}
	}
}
