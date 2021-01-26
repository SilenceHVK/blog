package me.hvkcoder.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * HDFS 文件批量上传
 *
 * @author h-vk
 * @since 2020/8/25
 */
public class BatchUploadFiles {
	// HDFS 上传根目录
	private static final String UPLOAD_ROOT_PATH = "hdfs://master-160:9000/tmp/";

	public static void main(String[] args) {
		// 读取配置文件
		Configuration conf = new Configuration(true);
		conf.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
		conf.set("fs.file.impl", "org.apache.hadoop.fs.LocalFileSystem");
		try {
			// 获取文件系统
			FileSystem fs = FileSystem.get(URI.create(UPLOAD_ROOT_PATH), conf, "root");

			// 按日期创建存储目录
			String uploadPath = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
			Path desPath = new Path(UPLOAD_ROOT_PATH + uploadPath);
			if (!fs.exists(desPath)) fs.mkdirs(desPath);

			// 上传本地文件
			LocalFileSystem local = FileSystem.getLocal(conf);
			FileStatus[] fileStatuses = local.globStatus(new Path("/Users/h-vk/Downloads/data/*"));
			Path[] paths = FileUtil.stat2Paths(fileStatuses);
			for (Path path : paths) fs.copyFromLocalFile(false, path, desPath);

			// 关闭文件系统
			local.close();
			fs.close();
			System.out.println("文件上传成功~");

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
  }
}
