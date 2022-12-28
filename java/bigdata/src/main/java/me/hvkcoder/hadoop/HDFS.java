package me.hvkcoder.hadoop;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;


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
	private static final String HDFS_ROOT_PATH = "hdfs://k8s-180:31900/source/";

	private FileSystem fileSystem;
	private Configuration conf;

	/**
	 * 连接 HDFS
	 */
	@Before
	public void connection2HDFS() throws IOException, InterruptedException {
//		// 设置配置文件
//		conf = new Configuration(true);
//		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
//		conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
//		conf.set("dfs.replication", "1");
//		conf.set("dfs.client.use.datanode.hostname", "true");
//
//		// 获取文件系统
//		fileSystem = FileSystem.get(URI.create(HDFS_ROOT_PATH), conf, "root");

		fileSystem = FileSystem.newInstance(new Configuration());
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

	/**
	 * 读取文件
	 *
	 * @throws IOException
	 */
	@Test
	public void catFile() throws IOException {
		try (FSDataInputStream fsDataInputStream = fileSystem.open(new Path("/hdfsapi/test/meta.properties"))) {
			IOUtils.copyBytes(fsDataInputStream, System.out, 1024);
		}
	}

	/**
	 * 向文件写入内容
	 *
	 * @throws IOException
	 */
	@Test
	public void writeFile() throws IOException {
		try (FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/hdfsapi/test/test.txt"))) {
			fsDataOutputStream.writeUTF("Hello HDFS!");
			fsDataOutputStream.flush();
		}
	}

	/**
	 * 上传大文件，并带有进度条
	 *
	 * @throws IOException
	 */
	@Test
	public void putBigFile() throws IOException {
		try (
			FileInputStream fileInputStream = new FileInputStream("/Users/h_vk/Downloads/游戏开发/百万在线 大型游戏服务端开发 (罗培羽) (z-lib.org).pdf");
			FSDataOutputStream outputStream = fileSystem.create(new Path("/hdfsapi/test/game.pdf"), () -> log.info("*"));
		) {
			IOUtils.copyBytes(fileInputStream, outputStream, 4096);
		}
	}

	/**
	 * 展示目录下所有文件
	 *
	 * @throws IOException
	 */
	@Test
	public void listFiles() throws IOException {
		FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/hdfsapi/test"));
		for (FileStatus fileStatus : fileStatuses) {
			log.info("文件名称 => {}", fileStatus.getPath().getName());
			log.info("文件路径 => {}", fileStatus.getPath().toUri().getPath());
			log.info("副本数量 => {}", fileStatus.getReplication());
			log.info("文件权限 => {}", fileStatus.getPermission());
			log.info("文件大小 => {}", fileStatus.getLen());
			log.info("是否为文件 => {}", fileStatus.isFile());
			log.info("是否为文件夹 => {}", fileStatus.isDirectory());
			log.info("--------------------------------");
		}
	}

	/**
	 * 递归展示目录信息
	 *
	 * @throws IOException
	 */
	@Test
	public void listFilesRecursive() throws IOException {
		RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(new Path("/hdfsapi"), true);
		while (remoteIterator.hasNext()) {
			LocatedFileStatus locatedFileStatus = remoteIterator.next();
			log.info("文件名称 => {}", locatedFileStatus.getPath().getName());
			log.info("文件路径 => {}", locatedFileStatus.getPath().toUri().getPath());
			log.info("副本数量 => {}", locatedFileStatus.getReplication());
			log.info("文件权限 => {}", locatedFileStatus.getPermission());
			log.info("文件大小 => {}", locatedFileStatus.getLen());
			log.info("是否为文件 => {}", locatedFileStatus.isFile());
			log.info("是否为文件夹 => {}", locatedFileStatus.isDirectory());
			log.info("--------------------------------");
		}
	}

	/**
	 * 实现 WordCount
	 *
	 * @throws IOException
	 */
	@Test
	public void wordCount() throws IOException {
		HashMap<String, Integer> wordCount = new HashMap<>();

		try (
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileSystem.open(new Path("/tmp/data.txt"))));
			FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/tmp/output_hdfs.txt"));
		) {
			// 读取HDFS文件数据
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				Arrays.stream(str.split(" ")).forEach(word -> {
					if (wordCount.containsKey(word)) {
						wordCount.put(word, wordCount.get(word) + 1);
					} else {
						wordCount.put(word, 1);
					}
				});
			}

			// 将统计的结果写入 HDFS 文件
			wordCount.forEach((word, count) -> {
				try {
					fsDataOutputStream.write((word + ":" + count + "\r\n").getBytes(StandardCharsets.UTF_8));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	@After
	public void close() throws IOException {
		if (fileSystem != null) {
			fileSystem.close();
		}
	}
}
