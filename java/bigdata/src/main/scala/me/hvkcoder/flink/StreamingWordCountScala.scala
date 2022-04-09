package me.hvkcoder.flink

import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

object StreamingWordCountScala {
	def main(args: Array[String]): Unit = {
		// 获取上下文
		val env = StreamExecutionEnvironment.getExecutionEnvironment
		// 创建数据源 Source
		val socketSource = env.socketTextStream("localhost", 9999)
		// 对数据源进行 Transformation
		socketSource.flatMap(_.split(" ")).map((_, 1)).keyBy(0).sum(1).print()
		env.execute("StreamingWorkCountFlink")
	}
}
