package me.hvkcoder.java_basic.jvm.proxy.test;

import me.hvkcoder.java_basic.jvm.proxy.CustomClassLoader;
import me.hvkcoder.java_basic.jvm.proxy.CustomInvocationHandler;
import me.hvkcoder.java_basic.jvm.proxy.CustomProxy;

import java.lang.reflect.Method;

/**
 * @author h_vk
 * @since 2022/4/2
 */
public class ProxyTest {
	public static void main(String[] args) {
		Person person = (Person) CustomProxy.newInstanceProxy(new CustomClassLoader(), new Class<?>[]{Person.class}, new CustomInvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Student student = new Student();
				System.out.println("代理类执行前......");
				Object result = method.invoke(student, args);
				System.out.println("代理类执行后......");
				return result;
			}
		});
		System.out.println(person.sayHello("张三", 18));
	}

	public static class Student implements Person {
		@Override
		public String sayHello(String name, Integer age) {
			return "Hello! 我的名字是:" + name + "，我的年龄:" + age;
		}
	}
}
