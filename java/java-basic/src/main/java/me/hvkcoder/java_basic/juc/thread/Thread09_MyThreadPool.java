package me.hvkcoder.java_basic.juc.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author h_vk
 * @since 2023/5/10
 */
public class Thread09_MyThreadPool {
	public static void main(String[] args) {
		ExecutorPool executor = new ExecutorPool(5);
		Stream.iterate(1, item -> item + 1).limit(10).forEach(item -> {
				executor.execute(() -> {
					System.out.println(Thread.currentThread().getName() + " execute this task");
				});
			}
		);
	}

	/**
	 * 自定义线程池
	 */
	private static class ExecutorPool {
		private final RunnableTaskQueue runnableTaskQueue;
		private final List<Thread> threads = new ArrayList<>();

		public ExecutorPool(int poolSize) {
			this.runnableTaskQueue = new RunnableTaskQueue();
			IntStream.rangeClosed(1, poolSize).forEach(index -> {
				if (threads.size() < poolSize) {
					Thread thread = new Thread(() -> {
						while (!Thread.currentThread().isInterrupted()) {
							try {
								Runnable task = runnableTaskQueue.getTask();
								task.run();
							} catch (InterruptedException e) {
								throw new RuntimeException(e);
							}
						}
					}, "custom-thread-poo-" + index);
					threads.add(thread);
					thread.start();
				}
			});
		}

		/**
		 * 执行任务
		 *
		 * @param runnable
		 */
		public void execute(Runnable runnable) {
			this.runnableTaskQueue.addTask(runnable);
		}
	}

	/**
	 * 任务队列
	 */
	private static class RunnableTaskQueue {

		/**
		 * 存储任务队列
		 */
		private final LinkedList<Runnable> tasks = new LinkedList<Runnable>();

		private final Lock lock = new ReentrantLock();
		private final Condition condition = lock.newCondition();

		/**
		 * 获取任务
		 *
		 * @return
		 * @throws InterruptedException
		 */
		public Runnable getTask() throws InterruptedException {
			lock.lock();
			try {
				while (tasks.isEmpty()) {
					condition.await();
				}
				return tasks.removeFirst();
			} finally {
				lock.unlock();
			}
		}

		/**
		 * 添加任务
		 *
		 * @param runnable
		 */
		public void addTask(Runnable runnable) {
			lock.lock();
			try {
				tasks.add(runnable);
				condition.signalAll();
			} finally {
				lock.unlock();
			}
		}
	}
}
