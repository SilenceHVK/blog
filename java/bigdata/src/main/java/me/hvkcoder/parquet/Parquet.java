package me.hvkcoder.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.column.ParquetProperties;
import org.apache.parquet.example.data.Group;
import org.apache.parquet.example.data.simple.SimpleGroupFactory;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.example.ExampleParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.LogicalTypeAnnotation;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.PrimitiveType;
import org.apache.parquet.schema.Types;

import java.io.IOException;
import java.util.UUID;

/**
 * Parquet 文件写入至 HDFS
 *
 * @author h_vk
 * @since 2021/9/14
 */
public class Parquet {

	/**
	 * Parquet Schema 定义
	 */
	private static final MessageType FILE_SCHEMA;
	private static final String TABLE_NAME = "parquet_practice";

	static {
		// 定义 Parquet 字段名称与字段类型
		final Types.MessageTypeBuilder messageTypeBuilder = Types.buildMessage();
		messageTypeBuilder
			.optional(PrimitiveType.PrimitiveTypeName.BINARY).as(LogicalTypeAnnotation.stringType()).named("id")
			.optional(PrimitiveType.PrimitiveTypeName.BINARY).as(LogicalTypeAnnotation.stringType()).named("name")
			.optional(PrimitiveType.PrimitiveTypeName.INT32).named("age");
		FILE_SCHEMA = messageTypeBuilder.named(TABLE_NAME);
	}

	public static void main(String[] args) {
		final Path path = new Path("hdfs://k8s-180:31900/source", System.currentTimeMillis() + ".parquet");
		final Configuration configuration = new Configuration();
		configuration.set("dfs.replication", "1");
		configuration.set("dfs.client.use.datanode.hostname", "true");

		final SimpleGroupFactory simpleGroupFactory = new SimpleGroupFactory(FILE_SCHEMA);
		try (
			final ParquetWriter<Group> parquetWriter = ExampleParquetWriter.builder(path)
				.withWriteMode(ParquetFileWriter.Mode.OVERWRITE)
				.withWriterVersion(ParquetProperties.WriterVersion.PARQUET_2_0)
				.withCompressionCodec(CompressionCodecName.SNAPPY)
				.withConf(configuration)
				.withType(FILE_SCHEMA)
				.build()
		) {
			for (int i = 0; i < 10; i++) {
				final Group group = simpleGroupFactory.newGroup();
				group.add("id", UUID.randomUUID().toString());
				group.add("name", UUID.randomUUID().toString() + "-" + i);
				group.add("age", 10 + i);
				parquetWriter.write(group);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
