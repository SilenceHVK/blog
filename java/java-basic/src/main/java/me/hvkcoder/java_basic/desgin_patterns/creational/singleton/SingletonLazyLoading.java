package me.hvkcoder.java_basic.desgin_patterns.creational.singleton;

/**
 * 单例模式懒汉式
 * <p>
 * 多线程访问时会出现问题,可以通过加入 synchronized 关键字加锁解决该问题，但带来了效率下降
 *
 * @author h-vk
 * @since 2020-02-10
 */
public class SingletonLazyLoading {
	private static volatile SingletonLazyLoading INSTANCE;

	private SingletonLazyLoading() {
	}

	public static synchronized SingletonLazyLoading getInstance() {
		if (INSTANCE == null) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			INSTANCE = new SingletonLazyLoading();
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		// 单例模式懒汉式
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(SingletonLazyLoading.getInstance())).start();
		}
	}
}
