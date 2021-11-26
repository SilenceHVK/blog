package me.hvkcoder.java_basic.desgin_patterns.creational.singleton;

/**
 * 单例模式饿汉式
 * <p>
 * 类加载到内存后，就实例化一个单例，JVM保证线程安全，容易造成资源浪费
 *
 * @author h-vk
 * @since 2020-02-07
 */
public class SingletonEH {

	private static final SingletonEH INSTANCE = new SingletonEH();

	private SingletonEH() {
	}

	public static SingletonEH getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) {
		// 单例模式饿汉式
		SingletonEH instanceEH_1 = SingletonEH.getInstance();
		SingletonEH instanceEH_2 = SingletonEH.getInstance();
		System.out.println(instanceEH_1 == instanceEH_2);
	}
}
