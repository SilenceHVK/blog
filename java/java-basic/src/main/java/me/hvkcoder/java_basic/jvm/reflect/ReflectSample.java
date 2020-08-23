package me.hvkcoder.java_basic.jvm.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TODO:
 *
 * @author h-vk
 * @since 2020/8/23
 */
public class ReflectSample {
  public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchFieldException {
		Class<?> aClass = Class.forName("me.hvkcoder.java_basic.jvm.reflect.Robot");

		/** TODO: 获取反射对象实例 */
		Robot robot = (Robot) aClass.getDeclaredConstructor().newInstance();

		/** TODO: 获取反射对象除 继承与接口的所有方法 */
		Method sayHello = aClass.getDeclaredMethod("sayHello", String.class);
		sayHello.setAccessible(true);
		Object privateMsg = sayHello.invoke(robot, "hvkcoder");
    System.out.println(privateMsg);

		/** TODO: 获取反射除私有的所有方法 */
		Method hi = aClass.getMethod("hi");
		hi.setAccessible(true);
		Object publicMsg = hi.invoke(robot);
    System.out.println(publicMsg);

		/** TODO: 获取反射对象 除继承的字段 */
		Field msg = aClass.getDeclaredField("msg");
		msg.setAccessible(true);
		msg.set(robot,"Silence H_VK");
		System.out.println(hi.invoke(robot));
	}
}
