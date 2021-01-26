package me.hvkcoder.java_basic.java8;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream (流) 使程序员可以站在更高的抽象层次上怼集合进行操作
 *
 * <p>如果返回值是 Stream，那么就是惰性求值；如果返回是另一个值或为空，那么就是及早求值
 *
 * <p>高阶函数：函数的参数列表包含函数接口，或该函数返回一个函数接口
 *
 * @author h-vk
 * @since 2020/8/9
 */
public class StreamUsage {
  public static void main(String[] args) {
    //  collect(toList()) 由 Stream 里的值生成一个列表，是一个及早求值操作
      List<String> collect = Stream.of("a", "b", "c").collect(Collectors.toList());
    System.out.println(collect);

    //  map() 可以将一个流中的值转换成一个新的流，是一个惰性求值
      List<String> mapResult =
        Stream.of("a", "b", "c").map(o -> o.toUpperCase()).collect(Collectors.toList());
    System.out.println(mapResult);

    //  filter() 接收 Predicate 接口，是一个惰性求值
      List<String> filterResult =
        Stream.of("Java", "C", "C++", "C#", "Python")
            .filter(o -> o.startsWith("C"))
            .collect(Collectors.toList());
    System.out.println(filterResult);

    //  flatMap() 可以用 Stream 替换值，然后将多个 Stream 连接成一个 Stream
      Set<String> singers =
        Stream.of(Arrays.asList("周杰伦", "林俊杰", "周传雄"), Arrays.asList("汪苏泷", "许嵩"))
            .flatMap(singer -> singer.stream())
            .filter(o -> o.startsWith("周"))
            .collect(Collectors.toSet());
    System.out.println(singers);

    //  reduce() 可以从一组值中生成一个值
      Integer count = Stream.of(1, 2, 3).reduce(0, (acc, el) -> acc + el);
    System.out.println(count);

    //  Optional 是为核心类库新设计的一个数据类型，在一个值可能为空的建模情况下，使用 Optional 对象能替代使用 null 值
      // -- 创建某个值的 Optional 对象
      Optional<String> a = Optional.of("a");
    System.out.println(a);
    // -- 创建一个空的 Optional 对象
    Optional<Object> empty = Optional.empty();
    System.out.println(empty);
    // -- 将一个 null 值转为 Optional 对象
    Optional<Object> o = Optional.ofNullable(null);
    System.out.println(o);
    // -- 使用 isPresent 方法验证验证一个 Optional 对象是否有值
    System.out.println(a.isPresent() + " , " + empty.isPresent());
    // -- orElse 方法，当 Optional 对象为空时，提供一个备选值
    System.out.println(empty.orElse("Hello"));
    // -- orElseGet 方法， 当 Optional 对象真正为空时才会调用，该方法接收一个 Supplier 对象
    System.out.println(empty.orElseGet(() -> "C"));
  }
}
