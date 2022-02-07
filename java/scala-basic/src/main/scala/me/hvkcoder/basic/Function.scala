package me.hvkcoder.basic

import java.util.Date

// Scala 高阶函数
object Function {

	// 定义成员方法
	def hello(): Unit = {
		println("Hello")
	}

	def main(args: Array[String]): Unit = {

		// 定义带返回值的函数
		def sayHello(): String = {
			// 在使用 return 关键字时，必须指定函数的返回类型
			return "Hello"
		}

		// 定义带参数的函数
		def sayHi(name: String = "hvkcoder") = {
			// 不使用 return 关键字时，函数返回值会被自动推断
			s"Hi, $name"
		}

		// 使用递归实现 Fibonacci 数列
		def fibonacciByRecursive(n: Int): Int = {
			if (n <= 2) return 1
			fibonacciByRecursive(n - 1) + fibonacciByRecursive(n - 2)
		}

		// 定义匿名函数
		def func: (Int, Int) => Int = (a: Int, b: Int) => {
			a + b
		}

		// 定义闭包，函数作为返回值
		def fibonacci(): () => Int = {
			var a = 0
			var b = 1
			() => {
				var tmp = a
				a = b
				b += tmp
				a
			}
		}

		val fib = fibonacci()
		println(fib())
		println(fib())
		println(fib())
		println(fib())
		println(fib())

		// 函数作为参数
		def strategyFunc(a: Int, b: Int, calc: (Int, Int) => Int) = {
			calc(a, b)
		}

		println(strategyFunc(1, 2, (a, b) => a + b))

		// 偏应用函数
		def log(date: Date, tp: String, msg: String): Unit = {
			println(s"$date\t$tp\t$msg")
		}

		def info = log(_: Date, "info", _: String)

		info(new Date(), "ok")

		// 可变参数
		def funcByArgs(n: Int*): Unit = {
			//			n.foreach((x) => {
			//				print(s"$x\t")
			//			})
			n.foreach(print(_))
		}

		funcByArgs(1, 2, 3, 4, 5, 6, 7, 8)

		// 柯里化
		def curring(str: String*)(num: Int*): Unit = {
			str.foreach(print(_))
			num.foreach(print(_))
		}

		curring("Hello")(18)

		println()
		// 方法需要赋值引用且不执行，使用 方法名+空格+_
		val helloFunc = hello _
	}
}
