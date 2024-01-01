package me.hvkcoder.java_basic.juc.practice;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多线程顺序打印 ABC
 *
 * @author h_vk
 * @since 2023/12/21
 */
public class PrintABC {
	private static Lock lock = new ReentrantLock();
	private static int num;

	private static void printABCByLock(int targetNum) {
		for (int i = 0; i < 10; ) {
			lock.lock();
			try {
				if (num % 3 == targetNum) {
					i++;
					num++;
					System.out.println(Thread.currentThread().getName());
				}
			} finally {
				lock.unlock();
			}
		}
	}

	private static void printABCByCondition(int targetNum, Condition currentCondition, Condition nextCondition) {
		for (int i = 0; i < 10; ) {
			lock.lock();
			try {
				while (num % 3 != targetNum) {
					currentCondition.await();
				}
				num++;
				i++;
				System.out.print(Thread.currentThread().getName());
				nextCondition.signal();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}
		}
	}

	private static void printABCBySemaphore(int targetNum, Semaphore currentSemaphore, Semaphore nextSemaphore) {
		for (int i = 0; i < 10; ) {
			try {
				currentSemaphore.acquire();
				if (num % 3 == targetNum) {
					i++;
					num++;
					System.out.print(Thread.currentThread().getName());
					nextSemaphore.release();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}


	public static void main(String[] args) {
//		new Thread(() -> printABCByLock(0), "A").start();
//		new Thread(() -> printABCByLock(1), "B").start();
//		new Thread(() -> printABCByLock(2), "C").start();

//		Condition conditionA = lock.newCondition();
//		Condition conditionB = lock.newCondition();
//		Condition conditionC = lock.newCondition();
//		new Thread(() -> printABCByCondition(0, conditionA, conditionB), "A").start();
//		new Thread(() -> printABCByCondition(1, conditionB, conditionC), "B").start();
//		new Thread(() -> printABCByCondition(2, conditionC, conditionA), "C").start();


		Semaphore semaphoreA = new Semaphore(1);
		Semaphore semaphoreB = new Semaphore(0);
		Semaphore semaphoreC = new Semaphore(0);

		new Thread(() -> printABCBySemaphore(0, semaphoreA, semaphoreB), "A").start();
		new Thread(() -> printABCBySemaphore(1, semaphoreB, semaphoreC), "B").start();
		new Thread(() -> printABCBySemaphore(2, semaphoreC, semaphoreA), "C").start();
	}
}
