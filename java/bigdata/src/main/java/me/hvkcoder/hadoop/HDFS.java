package me.hvkcoder.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * HDFS 操作
 *
 * @author h-vk
 * @since 2020/8/25
 */
@Slf4j
public class HDFS {

	/**
	 * HDFS 上传根目录
	 */
	private static final String HDFS_ROOT_PATH = "hdfs://192.168.56.180:31900/";

	private FileSystem fileSystem;
	private Configuration conf;

	/**
	 * 连接 HDFS
	 */
	@Before
	public void connection2HDFS() throws IOException, InterruptedException {
		// 设置配置文件
		conf = new Configuration(true);
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
		// 获取文件系统
		fileSystem = FileSystem.get(URI.create(HDFS_ROOT_PATH), conf, "root");
	}

	/**
	 * 创建文件夹
	 */
	@Test
	public void mkdir() throws IOException {
		final Path path = new Path("/test");

		// 判断文件夹是否存在，如果不存在则创建，存在则删除
		if (!fileSystem.exists(path)) {
			fileSystem.mkdirs(path);
		} else {
			fileSystem.delete(path, true);
		}
	}
	
	/**
	 * 上传文件
	 */
	@Test
	public void uploadFile() throws IOException {
		fileSystem.copyFromLocalFile(new Path("/Users/h_vk/Downloads/data/1631513425631"), new Path("/source"));
	}

	/**
	 * 批量上传文件
	 */
	@Test
	public void uploadBatchFile() throws IOException {
		// 按日期创建存储目录
		String uploadPath = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
		Path desPath = new Path(HDFS_ROOT_PATH + uploadPath);
		if (!fileSystem.exists(desPath)) fileSystem.mkdirs(desPath);

		// 上传本地文件
		try (final LocalFileSystem localFileSystem = FileSystem.getLocal(conf)) {
			FileStatus[] fileStatuses = localFileSystem.globStatus(new Path("/Users/h_vk/Downloads/data/*"));
			Path[] paths = FileUtil.stat2Paths(fileStatuses);
			for (Path path : paths) fileSystem.copyFromLocalFile(false, path, desPath);
		} finally {
			log.info("文件上传成功~");
		}
	}

	@After
	public void close() throws IOException {
		if (fileSystem != null) {
			fileSystem.close();
		}
	}
}
