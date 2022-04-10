package me.hvkcoder.java_basic.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 线程中断
 *
 * @author h_vk
 * @since 2022/4/12
 */
@Slf4j
public class Thread05_Interrupted {
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(() -> {
			// 获取线程 interrupted 标记
			while (!Thread.currentThread().isInterrupted()) {
				try {
					TimeUnit.SECONDS.sleep(1);
					log.info("线程正在执行.......");
				} catch (InterruptedException e) {
					// 当抛出 InterruptedException 时，interrupt 标志位将被复位 false
					log.info("线程已结束......");
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
		thread.start();
		
		TimeUnit.SECONDS.sleep(5);
		log.info("通知线程结束");
		thread.interrupt(); // 更改 interrupt 状态
	}
}
