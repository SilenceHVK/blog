package me.hvkcoder.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @author h_vk
 * @since 2022/4/6
 */
public class BatchWorkCountJava {
	public static void main(String[] args) throws Exception {
		// 获取上下文
		ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		// 创建数据源
		DataSource<String> dataSource = env.readTextFile(ClassLoader.getSystemResource("data.txt").getPath());
		// 对数据源进行 Transformation
		dataSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
			@Override
			public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
				Arrays.stream(value.split(" ")).forEach(world -> out.collect(new Tuple2<>(world, 1)));
			}
		}).groupBy(0).sum(1).print();
	}
}
