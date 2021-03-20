package me.hvkcoder.java_basic.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier 指定线程数执行完成，再执行后续操作，它的计数器可通过 reset() 方法重置
 *
 * @author h-vk
 * @since 2020/11/20
 */
@Slf4j
public class AQS02_CyclicBarrier {
	public static void main(String[] args) {
		final int count = 7;
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(count, () -> log.info("召唤神龙~~"));
		ExecutorService executor = Executors.newFixedThreadPool(count);

		for (int i = 1; i <= count; i++) {
			final int number = i;
			executor.execute(
				() -> {
					log.info("收集第 {} 个龙珠", number);
					try {
						System.out.println(cyclicBarrier.getNumberWaiting());
						cyclicBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						e.printStackTrace();
					}
				});
		}
		executor.shutdown();
	}
}
