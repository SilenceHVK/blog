package me.hvkcoder.basic

import scala.io.Source.{fromFile, fromURL}

object ReadFile {
	def main(args: Array[String]): Unit = {
		println("================= 按行读取文件 =================")
		val textFile = fromFile(ClassLoader.getSystemResource("data.txt").getPath)
		for (line <- textFile.getLines()) {
			println(line)
		}
		textFile.close()

		println("================= 读取网络文件 =================")
		val baidu = fromURL("http://www.baidu.com")
		for (line <- baidu.getLines()) {
			println(line)
		}
		baidu.close()
	}
}
