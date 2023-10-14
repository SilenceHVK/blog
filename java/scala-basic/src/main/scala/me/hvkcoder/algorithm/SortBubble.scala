package me.hvkcoder.algorithm

import scala.util.Random

// 冒泡排序
object SortBubble {
	def main(args: Array[String]): Unit = {
		val size = 10
		val numbers = new Array[Int](size)
		for (i <- 0 until size) {
			numbers(i) = Random.nextInt(400)
		}
		print("未排序的数据 => ")
		numbers.foreach(item => print(f"$item "))

		println()
		print("冒泡排序结果 => ")
		for (i <- 0 until numbers.length - 1) {
			for (j <- 0 until numbers.length - 1 - i) {
				if (numbers(j) > numbers(j + 1)) {
					val tmp = numbers(j)
					numbers(j) = numbers(j + 1)
					numbers(j + 1) = tmp
				}
			}
		}
		numbers.foreach(item => print(f"$item "))
	}
}
