package me.hvkcoder.java_basic.jvm.reflect.annotation.ch01;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author h-vk
 * @since 2020/6/23
 */
@Target(ElementType.METHOD) // 指定注解的作用范围
@Retention(RetentionPolicy.RUNTIME) // 指定 RUNTIME 级别保存注解
public @interface UseCase {
	public int id();

	public String description() default "no description";
}
