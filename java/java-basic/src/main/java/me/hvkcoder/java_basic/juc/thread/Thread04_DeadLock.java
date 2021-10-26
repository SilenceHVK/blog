package me.hvkcoder.java_basic.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 死锁
 *
 * @author h_vk
 * @since 2021/9/26
 */
@Slf4j
public class Thread04_DeadLock {
	public static void main(String[] args) {
		final Object A = new Object();
		final Object B = new Object();

		new Thread(() -> {
			synchronized (A) {
				try {
					TimeUnit.SECONDS.sleep(1);
					synchronized (B) {
						log.info("{} 获取到了两把锁", Thread.currentThread().getName());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "A").start();

		new Thread(() -> {
			synchronized (B) {
				try {
					TimeUnit.SECONDS.sleep(1);
					synchronized (A) {
						log.info("{} 获取到了两把锁", Thread.currentThread().getName());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "B").start();
	}
}
