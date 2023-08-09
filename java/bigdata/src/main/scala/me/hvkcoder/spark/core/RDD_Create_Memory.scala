package me.hvkcoder.spark.core

import org.apache.spark.{SparkConf, SparkContext}

object RDD_Create_Memory {
	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf().setMaster("local[*]").setAppName("RDD_Create_Memory")
		val sc = new SparkContext(sparkConf)

		// 从内存中创建 RDD，将内存中的集合作为要处理的数据源
		sc.parallelize(Seq[Int](1,2,3,4)).foreach(println)
		// 跟 parallelize 一样
		sc.makeRDD(Seq[Int](1,2,3,4)).foreach(println)

		// 从文件中创建 RDD，将文件中的数据作为要处理的数据源
		val fileRDD = sc.textFile(f"file://${ClassLoader.getSystemResource("data.txt").getPath}")
		println(fileRDD.count())

		sc.stop()
	}
}
