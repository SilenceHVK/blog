package me.hvkcoder.java_basic.leetcode.concurrency;

import java.util.concurrent.Semaphore;

/**
 * https://leetcode-cn.com/problems/print-in-order/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode1114_按序打印 {

	static class Foo {
		Semaphore semaphoreSecond = new Semaphore(0);
		Semaphore semaphoreThird = new Semaphore(0);

		public Foo() {

		}

		public void first(Runnable printFirst) throws InterruptedException {

			// printFirst.run() outputs "first". Do not change or remove this line.
			printFirst.run();
			semaphoreSecond.release();
		}

		public void second(Runnable printSecond) throws InterruptedException {
			// printSecond.run() outputs "second". Do not change or remove this line.
			semaphoreSecond.acquire();
			printSecond.run();
			semaphoreThird.release();
		}

		public void third(Runnable printThird) throws InterruptedException {

			// printThird.run() outputs "third". Do not change or remove this line.
			semaphoreThird.acquire();
			printThird.run();
		}
	}
}
