package me.hvkcoder.java_basic.juc.aqs;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Semaphore 信号量，同一时间只能有指定数量的线程执行，Semaphore 可以用于做流量控制
 *
 * @author h-vk
 * @since 2020/11/20
 */
@Slf4j
public class AQS03_Semaphore {
	public static void main(String[] args) {
		int count = 30;
		final Semaphore semaphore = new Semaphore(10);
		ExecutorService executorService = Executors.newFixedThreadPool(count);

		for (int i = 0; i < count; i++) {
			final int threadNum = i;
			executorService.execute(
				() -> {
					try {
						semaphore.acquire();
						log.info("save data = {}", threadNum);
						semaphore.release();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				});
		}
		executorService.shutdown();
	}

	/**
	 * 三个线程交替打印 ABC 10 次
	 */
	@Test
	public void testPrintABC() {
		final Semaphore semaphoreA = new Semaphore(1);
		final Semaphore semaphoreB = new Semaphore(0);
		final Semaphore semaphoreC = new Semaphore(0);

		Thread threadA =
			new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							semaphoreA.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + " - A");
						semaphoreB.release();
					}
				});

		Thread threadB =
			new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							semaphoreB.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + " - B");
						semaphoreC.release();
					}
				});

		Thread threadC =
			new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							semaphoreC.acquire();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + " - C");
						semaphoreA.release();
					}
				});

		threadA.start();
		threadB.start();
		threadC.start();
	}
}
