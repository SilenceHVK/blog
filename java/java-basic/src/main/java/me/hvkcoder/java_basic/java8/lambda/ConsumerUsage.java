package me.hvkcoder.java_basic.java8.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * TODO: java.util.function.Consumer<T> 接口定义了一个 accept 的方法，接收泛型 T 的对象，没有返回值
 *
 * <p>如果需要访问类型 T 的对象，并对其执行某些操作，就可以使用该接口
 *
 * @author h-vk
 * @since 2020-03-17
 */
public class ConsumerUsage {
  public static <T> void forEach(List<T> list, Consumer<T> consumer) {
    for (T t : list) {
      consumer.accept(t);
    }
  }

  public static void main(String[] args) {
    forEach(Arrays.asList("C", "C++", "Java", "C#"), System.out::println);
  }
}
