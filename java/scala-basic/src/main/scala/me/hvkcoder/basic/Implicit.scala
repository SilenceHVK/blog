package me.hvkcoder.basic

import scala.language.implicitConversions

class People {
	def run(): Unit = {
		println("我会奔跑~")
	}
}

class Superman {
	def fly(): Unit = {
		println("我会飞~")
	}
}

// 隐式转换： 在不更改逻辑代码的情况下，进行方法扩展
object Implicit {
	/**
	 * 隐式类的查找顺序：
	 * 1. 第一个当前作用域
	 * 2. 上级作用域
	 * 3. 当前类所有的包装类
	 * 4. 父类
	 * 5. trait
	 *
	 * @param person
	 */
	implicit class SpiderMan(people: People) {
		def spin(): Unit = {
			println("我会吐丝~")
		}
	}

	def main(args: Array[String]): Unit = {
		val people = new People()
		people.run()

		// 隐式方法
		implicit def transformObject(people: People): Superman = {
			new Superman()
		}

		people.fly()
		people.spin()

		println("-----------------")

		// 隐式参数
		def login(userName: String)(implicit password: String = "123456"): Unit = {
			println(s"用户名：$userName, 密码：$password")
		}

		// 隐式参数
		implicit val password: String = "987654321"
		login("hvkcoder") //使用隐式变量，如果未定义隐式变量，则使用隐式参数默认值
		login("hvkcoder")()
		login("hvkcoder")("555555")
	}
}
