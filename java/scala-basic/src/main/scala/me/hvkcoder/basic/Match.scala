package me.hvkcoder.basic

// Scala 匹配模式
object Match {
	// 自定义匹配模式函数
	def matchText(x: Int): String = x match {
		case 1 => "one"
		case 2 => "two"
		case _ => "many"
	}

	def main(args: Array[String]): Unit = {
		println(matchText(1))
		println("------------")

		val tuple: (String, Int, Int, Int, Boolean, Double) = ("Hello", 10, 4, 90, false, 1.1)
		tuple.productIterator.foreach {
			case s@"Hello" => println(s"Hello => $s")
			case o: Int if o > 50 => println(s"is Int and > 50 => $o")
			case o@(2 | 4 | 6 | 8) => println(s"is Even => $o")
			case false => println("is Boolean false")
			case x => println(s"is Other => $x")
		}

		// 偏应用函数实现
		val partialFunc: PartialFunction[Any, String] = {
			case "hello" => "hello"
			case x: Int => s"is Int $x"
			case _ => "any"
		}
	}
}
