package me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据表注解
 *
 * @author h-vk
 * @since 2020/10/5
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface DBTable {
    String value() default "";
}
