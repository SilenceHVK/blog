package me.hvkcoder.java_basic.leetcode;

import java.util.concurrent.Semaphore;

/**
 * https://leetcode-cn.com/problems/print-foobar-alternately/
 *
 * @author h-vk
 * @since 2020/11/18
 */
public class LeetCode1115_交替打印FooBar {
	public static void main(String[] args) {
		FooBar fooBar = new FooBar(2);
		new Thread(
			() -> {
				try {
					fooBar.foo(() -> System.out.println("foo"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			})
			.start();

		new Thread(
			() -> {
				try {
					fooBar.bar(() -> System.out.println("bar"));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			})
			.start();
	}

	static class FooBar {
		Semaphore semaphoreFoo = new Semaphore(1);
		Semaphore semaphoreBar = new Semaphore(0);

		private int n;

		public FooBar(int n) {
			this.n = n;
		}

		public void foo(Runnable printFoo) throws InterruptedException {

			for (int i = 0; i < n; i++) {
				semaphoreFoo.acquire();
				// printFoo.run() outputs "foo". Do not change or remove this line.
				printFoo.run();
				semaphoreBar.release();
			}
		}

		public void bar(Runnable printBar) throws InterruptedException {

			for (int i = 0; i < n; i++) {
				semaphoreBar.acquire();
				// printBar.run() outputs "bar". Do not change or remove this line.
				printBar.run();
				semaphoreFoo.release();
			}
		}
	}
}
