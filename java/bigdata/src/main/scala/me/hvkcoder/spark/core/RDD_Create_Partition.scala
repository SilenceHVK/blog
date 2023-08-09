package me.hvkcoder.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object RDD_Create_Partition {
	def main(args: Array[String]): Unit = {
		val sparkCon = new SparkConf().setMaster("local[*]").setAppName("RDD_Create_Partition")
		sparkCon.set("spark.default.parallelism", "5")
		val sc = new SparkContext(sparkCon)

		// 设置分区为 2，默认值分区为 8，值是从 spark.default.parallelism 获取的，并按分区存储文件
		sc.makeRDD(List(1, 2, 3, 4), 2).saveAsTextFile("test-output")

		sc.stop()
	}
}
