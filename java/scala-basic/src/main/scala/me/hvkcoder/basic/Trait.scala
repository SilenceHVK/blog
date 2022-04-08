package me.hvkcoder.basic

// Trait 类似于 Java 中的接口，与接口不同的是，Trait 中可以定义属性与方法的实现
// Scala 中类是单继承父类的，但父类是 Trait 可以多继承
trait Animal {
	def eat(): Unit = {
		println("吃饭~")
	}
}

class Human(var name: String, var age: Int) {
	def say(): Unit = {
		println("说话~")
	}
}

// Scala 中对于父类构造器有的属性可以不使用 var 或 val 声明，对于父类构造器没有的属性需要添加 var 或 val 声明
class Person(name: String, age: Int, var hobby: String) extends Human(name, age) with Animal {
	def playGaming(): Unit = {
		println("玩游戏~")
	}

	// 通过使用 override 对父类方法进行重写
	override def say(): Unit = {
		println(s"$name 正在说话")
	}
}

object Trait {
	def main(args: Array[String]): Unit = {
		val people: Person = new Person("张三", 18, "打篮球")
		people.eat()
		people.say()
		people.playGaming()
	}
}
