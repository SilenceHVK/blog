package me.hvkcoder.java_basic.desgin_patterns.creational.singleton;

/**
 * 单例模式枚举方式
 * <p>
 * 不仅可以解决线程同步，还可以防止反序列
 *
 * @author h-vk
 * @since 2020-02-10
 */
public enum SingletonEnum {

	INSTANCE;

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(SingletonEnum.INSTANCE.hashCode())).start();
		}
	}
}
