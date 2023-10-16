package me.hvkcoder.algorithm

import scala.util.Random

// 插入排序
object SortInsertion {
	def main(args: Array[String]): Unit = {
		val size = 10
		val numbers = new Array[Int](size)
		for (i <- 0 until size) {
			numbers(i) = Random.nextInt(100)
		}
		print("未排序的数据 => ")
		numbers.foreach(item => print(f"$item "))

		for (i <- numbers.indices) {
			for (j <- i until(0, -1); if (numbers(j) < numbers(j - 1))) {
				val tmp = numbers(j)
				numbers(j) = numbers(j - 1)
				numbers(j - 1) = tmp
			}
		}
		println()
		print("插入排序结果 => ")
		numbers.foreach(item => print(f"$item "))
	}
}
