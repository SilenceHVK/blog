package me.hvkcoder.parquet;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericData;
import org.apache.avro.reflect.ReflectData;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.LocalFileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.parquet.avro.AvroParquetReader;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetReader;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.hadoop.util.HadoopInputFile;
import org.apache.parquet.io.InputFile;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * @author h_vk
 * @since 2021/9/14
 */
@Slf4j
public class AvroParquet {

	/**
	 * 写入 Parquet 文件
	 */
	@Test
	public void avroParquetWriter() {
		final ArrayList<User> users = CollUtil.newArrayList(
			new User(UUID.randomUUID().toString(), "张三", 18),
			new User(UUID.randomUUID().toString(), "李四", 20),
			new User(UUID.randomUUID().toString(), "王五", 19),
			new User(UUID.randomUUID().toString(), "赵六", 22)
		);

		final Path filePath = new Path("/Users/h_vk/Downloads/tmp/" + System.currentTimeMillis() + ".parquet");

		// 写入 Parquet 文件
		try (
			ParquetWriter<User> parquetWriter = AvroParquetWriter.<User>builder(filePath)
				.withSchema(ReflectData.AllowNull.get().getSchema(User.class)) //Schema 格式
				.withDataModel(ReflectData.get())
				.withConf(new Configuration())  // Hadoop 配置
				.withCompressionCodec(CompressionCodecName.SNAPPY)
				.withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
				.build()
		) {
			for (User user : users) {
				parquetWriter.write(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 读取 Parquet 文件
	 *
	 * @throws IOException
	 */
	@Test
	public void avroParquetReader() throws IOException {
		// Hadoop 配置
		Configuration conf = new Configuration();
		conf.set("fs.hdfs.impl", DistributedFileSystem.class.getName());
		conf.set("fs.file.impl", LocalFileSystem.class.getName());
		
		InputFile inputFile = HadoopInputFile.fromPath(new Path("/Users/h_vk/Downloads/tmp/1631604823827.parquet"), conf);
		// 读取 Parquet 文件
		try (
			ParquetReader<GenericData.Record> parquetReader = AvroParquetReader.<GenericData.Record>builder(inputFile)
				.disableCompatibility()
				.withDataModel(GenericData.get())
				.withConf(conf)
				.build()
		) {
			GenericData.Record record;
			while ((record = parquetReader.read()) != null) {
				log.info("{}", record);
			}
		}
	}
}
