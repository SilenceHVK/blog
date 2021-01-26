package me.hvkcoder.java_basic.leetcode.concurrency;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

/**
 * https://leetcode-cn.com/problems/print-zero-even-odd/
 *
 * @author h-vk
 * @since 2020/11/18
 */
public class LeetCode1116_打印零与奇偶数 {


	static class ZeroEvenOdd {
		Semaphore semaphoreZero = new Semaphore(1);
		Semaphore semaphoreEven = new Semaphore(0);
		Semaphore semaphoreOdd = new Semaphore(0);

		private int n;

		public ZeroEvenOdd(int n) {
			this.n = n;
		}

		// printNumber.accept(x) outputs "x", where x is an integer.
		public void zero(IntConsumer printNumber) throws InterruptedException {
			for (int i = 1; i <= n; i++) {
				semaphoreZero.acquire();
				printNumber.accept(0);
				if (i % 2 == 0) {
					semaphoreEven.release();
				} else {
					semaphoreOdd.release();
				}
			}
		}

		public void even(IntConsumer printNumber) throws InterruptedException {
			for (int i = 2; i <= n; i += 2) {
				semaphoreEven.acquire();
				printNumber.accept(i);
				semaphoreZero.release();
			}
		}

		public void odd(IntConsumer printNumber) throws InterruptedException {
			for (int i = 1; i <= n; i += 2) {
				semaphoreOdd.acquire();
				printNumber.accept(i);
				semaphoreZero.release();
			}
		}
	}
}
