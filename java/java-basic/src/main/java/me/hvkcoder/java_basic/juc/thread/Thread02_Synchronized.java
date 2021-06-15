package me.hvkcoder.java_basic.juc.thread;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * synchronized 同步代码块
 * <p>
 * Java 中的所有对象都可以作为锁，具体表现为以下 3 种形式
 * 1. 对于普通方法块，锁的是当前实例对象
 * 2. 对于同步方法块，锁的是 synchronized 括号中的对象
 * 3. 对于静态同步方法，锁的是 是当前类的 Class 对象
 *
 * @author h-vk
 * @since 2021/5/18
 */
public class Thread02_Synchronized {

	public static class Goods {
		private int count = 0;

		@SneakyThrows
		public synchronized void increment() {
			while (count != 0) {
				this.wait();
			}

			count = 1;
			System.out.println(Thread.currentThread().getName() + " 生产了数据 -> " + count);
			this.notifyAll();
		}

		@SneakyThrows
		public synchronized void decrement() {
			while (count == 0) {
				this.wait();
			}

			count = 0;
			System.out.println(Thread.currentThread().getName() + " 消费了数据 -> " + count);
			this.notifyAll();
		}
	}

	@Test
	public void testProducerAndConsumer() {
		Goods goods = new Goods();
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				goods.increment();
			}
		}, "A").start();
		new Thread(() -> {
			for (int i = 0; i < 10; i++) {
				goods.decrement();
			}
		}, "B").start();
	}
}
