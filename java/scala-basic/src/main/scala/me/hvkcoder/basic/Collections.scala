package me.hvkcoder.basic

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

// Scala 集合使用
// Scala 可以使用 Java 中的数据结构
object Collections {
	def main(args: Array[String]): Unit = {
		// Scala 中泛型写在[]中
		val language = Array[String]("Java", "Golang", "Python")
		println(s"获取数组下标为 1 => ${language(1)}")
		// 更改数组下标为 2 的值
		language(2) = "python";

		// 遍历数组元素
		for (elem <- language) {
			println(elem)
		}
		println("========= 通过 foreach 遍历元素 ===========")
		language.foreach(println)

		println("========= 不可变数据集 ===========")
		// 链表 => Scala 中数据集分为两类，immutable（不可变数据集） 和 mutable（可变数据集），默认使用 immutable
		val immutableList = List("Java", "Golang", "Python", "JavaScript")
		language.foreach(println)

		println("========= 变数据集 ===========")
		val mutableList = ListBuffer[String]()
		for (i <- 1 to 10) {
			// 通过 .+=() 添加元素
			mutableList.+=(s"数据$i")
		}
		mutableList.foreach(println)

		println("========= 不可变 Set ===========")
		val immutableSet = Set(1, 2, 3, 4, 5, 4, 3, 2, 1)
		immutableSet.foreach(println)

		println("========= 可变 Set ===========")
		val mutableSet = mutable.Set(1, 2, 3, 4, 5, 4, 3, 2, 1)
		mutableSet.add(10)
		mutableSet += 100
		mutableSet.remove(1)
		mutableSet -= 5
		mutableSet.foreach(println)

		println("========= Tuple 元组 ===========")
		val tuple2 = new Tuple2("hvkcoder", 18)
		val tuple3 = Tuple3("hvkcoder", 18, "Male")
		val tuple4 = ("hvkcoder", 18, "Male", "123456")
		println(tuple4._1)

		println("========= 迭代器 ===========")
		val iterator = language.iterator
		while (iterator.hasNext) {
			println(iterator.next())
		}

		println("========= 不可变 Map ===========")
		val singer = Map(("周杰伦", "青花瓷"), "许嵩" -> "玫瑰花的葬礼")
		println(s"获取 map 所有的 key => ${singer.keys}")
		println(s"根据 key 获取对应的 value => ${singer.getOrElse("周杰伦", "未找到")}")
		println(s"根据 key 获取对应的 value => ${singer.getOrElse("汪苏泷", "未找到")}")
		singer.keys.foreach(key => println(s"${key}:${singer(key)}"))

		val mutableMap = mutable.Map(("周杰伦", "青花瓷"), "许嵩" -> "玫瑰花的葬礼")
		mutableMap.put("汪苏泷", "不分手的恋爱")

		println("========= 集合的应用 ===========")
		// 在每一步执行过程中都将产生新的数据副本，内存成N被扩大
		val strValue = List("hello world", "hello google", "hello bing", "hello baidu")
		val flatMap = strValue.flatMap((s: String) => s.split(" "))
		val tuple = flatMap.map((s: String) => (s, 1))
		tuple.foreach(print)

		println("\n========= 集合的应用2 ===========")
		// 使用迭代器解决产生新的数据副本问题，迭代器中是不存储数据的
		val strValues = List("hello world", "hello google", "hello bing", "hello baidu")
		val flatMapValue = strValues.iterator.flatMap(_.split(" ")) // 迭代器下 flatMap 没有发生计算，而是产生了一个新的迭代器对象
		val tupleValue = flatMapValue.map((_, 1))
		tupleValue.foreach(print)


	}
}
