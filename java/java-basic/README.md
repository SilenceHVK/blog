## Java 8

####  常用的函数式接口

| 接口                | 方法              | 使用案例               |
| ------------------- | ----------------- | ---------------------- |
| Predicate<T>        | boolean test(T t) | 布尔表达式             |
| Consumer<T>         | void accept(T t)  | 输出一个对象值         |
| Function<T,R>       | R apply(T t)      | 从一个对象中选择或提取 |
| Supplier<T>         | T get();          | 创建一个对象           |
| BiFunction<T, U, R> | R apply(T t, U u) | 合并或比较两个对象     |



