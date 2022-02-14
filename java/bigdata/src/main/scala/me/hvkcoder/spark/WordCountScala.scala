package me.hvkcoder.spark

import org.apache.spark.{SparkConf, SparkContext}

object WordCountScala {
	def main(args: Array[String]): Unit = {
		val sparkConf = new SparkConf()
		sparkConf.setAppName("WorkCount")
		sparkConf.setMaster("local") // 设置单机本地运行

		val sparkContext = new SparkContext(sparkConf)
		val textRdd = sparkContext.textFile(ClassLoader.getSystemResource("data.txt").getPath)
		val result = textRdd.flatMap(_.split(" ")).map((_, 1)).reduceByKey((oldValue, value) => oldValue + value)
		result.foreach(println)
	}
}
