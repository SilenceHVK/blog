package me.hvkcoder.java_basic.desgin_patterns.creational.singleton;

/**
 * 静态内部类方式
 * <p>
 * JVM 保证单例，加载外部类时不会加载内部类，这样可以实现懒加载
 *
 * @author h-vk
 * @since 2020-02-10
 */
public class SingletonInnerClass {
	private SingletonInnerClass() {
	}

	public static SingletonInnerClass getInstance() {
		return SingletonInnerClassHolder.INSTANCE;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(SingletonInnerClass.getInstance())).start();
		}
	}

	private static class SingletonInnerClassHolder {
		public static final SingletonInnerClass INSTANCE = new SingletonInnerClass();
	}
}
