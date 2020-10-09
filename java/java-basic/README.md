## Java 8

####  常用的函数式接口

| 接口                | 方法              | 使用案例               |
| ------------------- | ----------------- | ---------------------- |
| Predicate<T>        | boolean test(T t) | 布尔表达式             |
| Consumer<T>         | void accept(T t)  | 输出一个对象值         |
| Function<T,R>       | R apply(T t)      | 从一个对象中选择或提取 |
| Supplier<T>         | T get();          | 创建一个对象           |
| BiFunction<T, U, R> | R apply(T t, U u) | 合并或比较两个对象     |


## Java 中的注解

注解（也被称为 元数据）为我们在代码中添加信息提供了一种形式化的方法，使我们可以在稍后某个时刻非常方便地使用这些数据。

#### Java SE5 内置三种标准注解

定义在 java.lang 中的注解：
- @Override：表示当前的方法定义将覆盖父类中的方法；
- @Deprecated：被标注的元素，编译器会发出警告信息；
- @SuppressWarnings：关闭不当的编译器警告信息；

如果在创建描述符性质的类或接口时，一旦其中包含了重复的工作，那就可以考虑使用注解来简化与自动化该过程

#### 注解的定义

注解的定义与接口的定义类似，事实上，注解也将会编译成 class 文件

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseCase {
    // 定义注解元素
    public int id();
    // 定义注解元素，并附默认值
    public String description() default "no description";
}
```

注解元素可用类型如下所示：

- 所有基本类型（int、float、boolean等）
- String
- enum
- Annotation
- 以上类型的数组

如果使用其他类型，编译器就会报错，同样也不允许使用任何包装类型。注解类型不能有不确定的值，也就是说，必须有默认值 或 使用注解时提供的元素的值；对于非基本类型的注解元素，无论是在源代码中声明时，或是在注解接口中定义默认值时，都不能使用 null 作为其值。

#### Java 提供四种元注解

用于注解其他的注解

| 注解名称   | 描述                                                         |
| ---------- | ------------------------------------------------------------ |
| @Target    | 描述注解的使用范围，可选的 ElementType 参数包括：<br/>CONSTRUCTOR：构造器声明；<br/>FIELD：域声明（包括 enum 实例）；<br/>LOCAL_VARIABLE：局部变量声明；<br/>METHOD：方法声明；<br/>PACKAGE：包声明；<br/>PARAMETER：参数声明；<br/>TYPE：类、接口（包括注解类型）或 enum声明； |
| @Retention | 表示需要在什么级别保存该注解信息，可选的 RetentionPolicy 参数包括：<br/>Source：注解将被编译器丢弃；<br/>CLASS：注解在 class 文件可用，但会被 JVM 丢弃；<br/>RUNTIME：JVM 将在运行期也保留注解，因此可以通过反射机制读取注解的信息； |
| @Document  | 该注解将被包含在 javadoc 中                                  |
| @Inherited | 允许子类继承父类的注解                                       |

#### 注解处理器

大部分情况下，需要我们自己编写处理器来处理注解，其核心则是使用 Java 的反射机制来处理注解。

```java
public static void trackUseCases(Class<?> cl) {
    // 通过反射 返回类中除继承的所有方法
    for(Method method : cl.getDeclaredMethods()) {
        // 通过反射 返回指定类型的注解对象
        UseCase annotation = method.getAnnotation(UseCase.class);
        if(annotation)！= null) {
            System.out.printf("Found Use Case: %d, %s\n", annotation.id(), annoation.description());
        }
    }
}
```
