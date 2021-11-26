package me.hvkcoder.java_basic.desgin_patterns.creational.singleton;

/**
 * 单例模式 懒汉式
 * <p>
 * 通过双重检查 + 代码块上锁，解决多线程获得的对象不是同一对象问题
 *
 * @author h-vk
 * @since 2020-02-10
 */
public class SingletonLH {
	private static volatile SingletonLH INSTANCE;

	private SingletonLH() {
	}

	public static SingletonLH getInstance() {
		// 双重检查
		if (INSTANCE == null) {
			// 减小同步代码块
			synchronized (SingletonLH.class) {
				if (INSTANCE == null) {
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					INSTANCE = new SingletonLH();
				}
			}
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			new Thread(() -> System.out.println(SingletonLH.getInstance())).start();
		}
	}
}
