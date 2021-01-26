package me.hvkcoder.java_basic.java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * java.util.function.Predicate<T> 接口定义了一个 test 方法，接收一个泛型对象 T，并返回一个 boolean
 *
 * <p>在需要表示一个涉及类型 T 的 boolean 表达式时，就可以使用该接口
 *
 * @author h-vk
 * @since 2020-03-17
 */
public class PredicateUsage {
  public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
    ArrayList<T> result = new ArrayList<>();
    for (T t : list) {
      if (predicate.test(t)) result.add(t);
    }
    return result;
  }

  public static void main(String[] args) {
    List<String> languages = Arrays.asList("C", "C++", "C#", "Java", "Python", "Golang", "Ruby");
    List<String> result = filter(languages, (String s) -> s.startsWith("C"));
    System.out.println(result);
  }
}
