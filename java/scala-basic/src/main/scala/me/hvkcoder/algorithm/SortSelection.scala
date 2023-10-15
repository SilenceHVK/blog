package me.hvkcoder.algorithm

import scala.util.Random

// 选择排序
object SortSelection {
	def main(args: Array[String]): Unit = {
		val size = 10
		val numbers = new Array[Int](size)
		for (i <- 0 until size) {
			numbers(i) = Random.nextInt(100)
		}
		print("未排序的数据 => ")
		numbers.foreach(item => print(f"$item "))

		for (i <- numbers.indices) {
			var min = i
			for (j <- i + 1 until numbers.length) {
				if (numbers(min) > numbers(j)) {
					min = j
				}
			}

			if (min != i) {
				val tmp = numbers(i)
				numbers(i) = numbers(min)
				numbers(min) = tmp
			}
		}
		println()
		print("选择排序结果 => ")
		numbers.foreach(item => print(f"$item "))
	}
}
