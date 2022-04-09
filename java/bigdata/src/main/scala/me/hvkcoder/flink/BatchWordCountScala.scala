package me.hvkcoder.flink

import org.apache.flink.api.scala.{ExecutionEnvironment, createTypeInformation}

object BatchWordCountScala {
	def main(args: Array[String]): Unit = {
		// 获取上下文
		val env = ExecutionEnvironment.getExecutionEnvironment
		// 创建数据源 Source
		val fileSource = env.readTextFile(ClassLoader.getSystemResource("data.txt").getPath)
		// 对数据源进行 Transformation
		fileSource.flatMap(_.split(" ")).map((_, 1)).groupBy(0).sum(1).print()
	}
}
