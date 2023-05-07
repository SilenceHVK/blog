package me.hvkcoder.java_basic.juc.thread;

import lombok.SneakyThrows;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author h_vk
 * @since 2023/5/9
 */
public class Thread08_CustomThreadPool {
	@SneakyThrows
	public static void main(String[] args) {
		PausedThreadPool pausedThreadPool = new PausedThreadPool(10, 20, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		
		for (int i = 0; i < 10000; i++) {
			pausedThreadPool.execute(() -> {
				System.out.println("线程正在执行");
				try {
					TimeUnit.MICROSECONDS.sleep(20);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
		}
		TimeUnit.MICROSECONDS.sleep(1500);
		pausedThreadPool.pause();
		System.out.println("线程池被暂停");
		TimeUnit.SECONDS.sleep(2);
		pausedThreadPool.resume();
		System.out.println("线程池继续工作");
		pausedThreadPool.shutdown();

	}

	/**
	 * 自定义可暂停的线程池
	 */
	private static class PausedThreadPool extends ThreadPoolExecutor {
		/**
		 * 线程暂停标志位
		 */
		private boolean isPaused;

		private final Lock lock = new ReentrantLock();
		private final Condition unPaused = lock.newCondition();

		public PausedThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		}

		/**
		 * 线程执行前钩子函数
		 *
		 * @param t the thread that will run task {@code r}
		 * @param r the task that will be executed
		 */
		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			super.beforeExecute(t, r);
			lock.lock();
			try {
				while (isPaused) {
					unPaused.await();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			} finally {
				lock.unlock();
			}
		}

		/**
		 * 暂停线程
		 */
		private void pause() {
			lock.lock();
			try {
				isPaused = true;
			} finally {
				lock.unlock();
			}
		}

		/**
		 * 恢复暂停的线程
		 */
		private void resume() {
			lock.lock();
			try {
				isPaused = false;
				unPaused.signalAll();
			} finally {
				lock.unlock();
			}
		}
	}
}
