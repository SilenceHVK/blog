package me.hvkcoder.basic

// Trait 类似于 Java 中的接口，与接口不同的是，Trait 中可以定义属性与方法的实现
// Scala 中类是单继承父类的，但父类是 Trait 可以多继承
trait Animal {
	def eat(): Unit = {
		println("吃饭~")
	}
}

trait Human {
	def say(): Unit = {
		println("说话~")
	}
}

class Person extends Human with Animal {
	def playGaming(): Unit = {
		println("玩游戏~")
	}
}

object Trait {
	def main(args: Array[String]): Unit = {
		val people: Person = new Person()
		people.eat()
		people.say()
		people.playGaming()
	}
}
