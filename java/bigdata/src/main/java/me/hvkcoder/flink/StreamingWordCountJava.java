package me.hvkcoder.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;

/**
 * @author h_vk
 * @since 2022/4/6
 */
public class StreamingWordCountJava {
	public static void main(String[] args) throws Exception {
		// 获取上下文
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 创建数据源 Source
		DataStreamSource<String> streamSource = env.socketTextStream("localhost", 9999);
		// 对数据源进行 Transformation
		streamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
				@Override
				public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
					Arrays.stream(value.split(" ")).forEach(world -> out.collect(new Tuple2<>(world, 1)));
				}
			}).keyBy(value -> value.f0)
			.sum(1)
			.print()
		;
		env.execute("StreamingWorkCountJava");
	}
}
