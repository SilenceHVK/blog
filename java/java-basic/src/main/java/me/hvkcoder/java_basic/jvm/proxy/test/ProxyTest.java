package me.hvkcoder.java_basic.jvm.proxy.test;

import me.hvkcoder.java_basic.jvm.proxy.jdk.CustomClassLoader;
import me.hvkcoder.java_basic.jvm.proxy.jdk.CustomInvocationHandler;
import me.hvkcoder.java_basic.jvm.proxy.jdk.CustomProxy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author h_vk
 * @since 2022/4/2
 */
public class ProxyTest {
	public static void main(String[] args) {

		// JDK 动态代理
		Person personJDK = (Person) Proxy.newProxyInstance(ProxyTest.class.getClassLoader(), new Class<?>[]{Person.class}, new InvocationHandler() {
			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				Student student = new Student();
				System.out.println("代理类执行前......");
				Object result = method.invoke(student, args);
				System.out.println("代理类执行后......");
				return result;
			}
		});
		System.out.println(personJDK.sayHello("王五", 25));
		System.out.println();

		// 手写实现 JDK 动态代理
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
		System.out.println();

		// 使用 CGLib 实现动态代理
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(Superman.class);
		enhancer.setCallback(new MethodInterceptor() {
			@Override
			public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
				System.out.println("代理类执行前......");
				Object result = methodProxy.invokeSuper(o, args);
				System.out.println("代理类执行后......");
				return result;
			}
		});
		Superman superman = (Superman) enhancer.create();
		System.out.println(superman.sayHello());
	}

	public static class Student implements Person {
		@Override
		public String sayHello(String name, Integer age) {
			return "Hello! 我的名字是:" + name + "，我的年龄:" + age;
		}
	}

	public static class Superman {
		public String sayHello() {
			return "Hello! 我是超人.....";
		}
	}
}
