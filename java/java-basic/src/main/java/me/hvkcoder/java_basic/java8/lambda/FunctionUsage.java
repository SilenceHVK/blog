package me.hvkcoder.java_basic.java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * java.util.function.Function<T, R> 接口定义了一个 apply 方法，它接受一个泛型 T 对象，并返回一个泛型 R 的对象
 *
 * <p>如果需要定义一个 Lambda ，将输入对象的信息映射到输出，就可以使用该接口
 *
 * @author h-vk
 * @since 2020/7/25
 */
public class FunctionUsage {
	public static void main(String[] args) {
		List<String> languages = Arrays.asList("C", "C#", "C++", "Java", "Golang", "Python", "Ruby");
		List<Integer> result = map(languages, String::length);
		System.out.println(result);
	}

	public static <T, R> List<R> map(List<T> lists, Function<T, R> func) {
		List<R> result = new ArrayList<>();
		for (T t : lists) {
			result.add(func.apply(t));
		}
		return result;
	}
}
