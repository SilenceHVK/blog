package me.hvkcoder.java_basic.jvm.reflect.annotation.ch01;

import java.lang.reflect.Method;

/**
 * 注解处理器，使用Java的反射机制实现
 *
 * @author h-vk
 * @since 2020/6/23
 */
public class UseCaseTracker {
	public static void trackUseCases(Class<?> cl) {
		//  通过反射 返回类中除 继承的所有方法
		for (Method method : cl.getDeclaredMethods()) {
			//  通过反射 返回指定类型的注解对象
			UseCase annotation = method.getAnnotation(UseCase.class);
			if (annotation != null) {
				System.out.printf("Found Use Case：%d, %s\n", annotation.id(), annotation.description());
			}
		}
	}

	public static void main(String[] args) {
		trackUseCases(PasswordUtils.class);
	}
}
