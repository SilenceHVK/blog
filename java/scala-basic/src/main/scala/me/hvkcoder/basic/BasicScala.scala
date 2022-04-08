package me.hvkcoder.basic

// 如果 class 名称与 object 名称一致，则两者为伴生关系
// 类名构造器中的参数，就是该类的成员属性，且默认是 val 常量，且默认是 private
// 只有类名构造器中的参数可以设置为 var，其他方法函数的参数都是 val，且不允许设置为 var
class BasicScala(var sex: String) {
	// var 关键字将自动推断变量类型
	var name = "hvkcoder"
	// 可以通过 :类型 给定类型
	var age: Int = 18
	// val 定义一个常量
	val PI = 3.1415926

	// 定义私有变量，即只能类内部访问
	private[this] var password: String = _

	// 定义构造函数
	def this(name: String, sex: String) {
		// 在自定义构造函数中必须调用默认构造方法
		this(sex)
		this.name = name
	}

	// 执行顺序 => 1
	println("class start.")

	// 使用 def 定义方法
	def sayHi(): Unit = {
		// 字符串前添加 s 可以实现字符串拼接，通过 $ 符号调用变量，或使用${} 对变量进行操作
		println(s"Hi! My name's $name，${age + 2} years old, $sex")
	}
	// 执行顺序 => 2
	println("class end.")


	def apply(): Unit = {
		println("class apply......")
	}
}

// object 类似于 java 中的 static
object BasicScala {
	// 被定义在方法外的语句，将被当做默认构造方法内的语句优先执行
	var people: BasicScala = new BasicScala("hvkcoder", "男");
	// 执行顺序 => 3
	println("object start.")

	def apply(): Unit = {
		// 一般是在都是 object apply 中实例对象
		println("object apply......")
	}

	// Unit 类似于 java 中的 void
	def main(args: Array[String]): Unit = {
		// 执行顺序 => 5
		println("Hello  Scala")
		// 执行顺序 => 6
		people.sayHi()

		// 使用 类名() ==> object.apply()
		val objectBasicScala = BasicScala()
		// 使用 对象名() ==> class.apply()
		val classBasicScala = new BasicScala("男")
		classBasicScala()
	}
	// 执行顺序 => 4
	println("object end.")
}
