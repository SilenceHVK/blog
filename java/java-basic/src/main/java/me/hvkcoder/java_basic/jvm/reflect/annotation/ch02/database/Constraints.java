package me.hvkcoder.java_basic.jvm.reflect.annotation.ch02.database;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义数据表字段的约束
 *
 * @author h-vk
 * @since 2020/6/23
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraints {
	/**
	 * 是否为主键
	 */
	boolean primaryKey() default false;

	/**
	 * 是否为空
	 */
	boolean allowNull() default false;

	/**
	 * 是否为唯一值
	 */
	boolean unique() default false;
}
