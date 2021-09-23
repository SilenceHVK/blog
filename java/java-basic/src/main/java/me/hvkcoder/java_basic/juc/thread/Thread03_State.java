package me.hvkcoder.java_basic.juc.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Thread 生命周期
 *
 * @author h_vk
 * @since 2021/9/23
 */
@Slf4j
public class Thread03_State {

	/**
	 * 输出线程 New、Runnable、Terminated 状态
	 */
	@Test
	public void testNewRunnableTerminated() {
		Thread thread = new Thread();
		// New 状态，线程只进行了创建并没有启用
		log.info("{}", thread.getState());
		thread.start();

		// Runnable 状态，创建的线程调用 start 方法，等待CPU调度
		log.info("{}", thread.getState());
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// Terminated 状态，线程正常退出，或线程出现异常
		log.info("{}", thread.getState());
	}

	/**
	 * 输出线程 Blocked、Waiting、Timed_Waiting 状态
	 */
	@Test
	public void testBlockedWaitingTimedWaiting() throws InterruptedException {
		Thread thread1 = new Thread(() -> sync());
		Thread thread2 = new Thread(() -> sync());
		thread1.start();
		thread2.start();

		TimeUnit.SECONDS.sleep(1);

		// Timed_Waiting 状态，在线程执行中调用了 sleep 方法
		log.info("{}", thread1.getState());
		// Blocked 状态，由于 sync 方法使用 synchronized 修饰，且 thread1 优先于 thread2 拿到锁
		log.info("{}", thread2.getState());

		TimeUnit.SECONDS.sleep(3);
		// Waiting 状态，在线程执行中调用了 wait 方法
		log.info("{}", thread1.getState());
	}

	public synchronized void sync() {
		try {
			TimeUnit.SECONDS.sleep(2);
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


}
