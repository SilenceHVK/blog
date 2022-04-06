package me.hvkcoder.basic

import scala.language.postfixOps
import scala.util.control.Breaks.{break, breakable}

// Scala 流程控制
object ProcessControl {
	def main(args: Array[String]): Unit = {
		// if 判断与 java 的 if 使用一致
		val sex = "Male"
		if (sex.eq("Male")) {
			println("男士")
		} else {
			println("女士")
		}

		// while 与 java 中 while 用法一致
		var count = 0
		while (count > 10) {
			// Scala 中不支持 ++ 操作
			count += 1
		}

		// 生成 1-10 的集合，且步进为 2, to 表示 1 <= to <= 10
		val data: Range.Inclusive = 1 to(10, 2)
		// 生成 1-9 的集合 until 表示 1<= until < 10
		val data2 = 1 until 10

		// Scala 中的 for 循环
		// Scala 循环中没有 break，可以通过循环逻辑进行判断
		for (i <- (1 to 100) if (i % 2 == 0)) {
			println(i)
		}
		// Scala 中一切皆是对象，因此通过调用 break() 抛出 BreakException 异常来终止循环
		var n = 0
		breakable { // breakable 类似于 Java中的 try-catch
			while (n <= 20) {
				n += 1
				if (n == 8) {
					break()
				}
			}
		}
		println(n)

		println("==========================================")
		// 倒序循环
		for (n <- 1 to 3 reverse) {
			println(n)
		}
		println("==========================================")
		// 使用 Scala for 循环实现九九乘法表
		for (i <- 1 to 9; j <- 1 to 9 if (j <= i)) {
			if (j <= i) print(s"$j x $i = ${i * j}\t")
			if (i == j) println()
		}

		println("==========================================")
		// 使用 yield 收集循环中的元素，并可以对该元素做更改，类似于 lambda map 操作
		val data3 = for (i <- 1 to 10) yield {
			i + 2
		}
		println(data3)
	}
}
