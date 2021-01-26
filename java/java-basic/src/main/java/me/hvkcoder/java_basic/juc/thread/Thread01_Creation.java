package me.hvkcoder.java_basic.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * 线程创建方式
 *
 * @author h-vk
 * @since 2020/11/13
 */
@Slf4j
public class Thread01_Creation {

	/**
     *  使用继承 Thread 类的方式创建线程
     */
    public static class CreateThreadByExtend extends Thread {
		public static void main(String[] args) {
			CreateThreadByExtend createThreadByExtend = new CreateThreadByExtend();
			createThreadByExtend.start();
		}

		@Override
		public void run() {
			log.info("使用继承 Thread");
		}
	}

	/**
     *  使用实现 Runnable 接口的方式创建线程，一般用于无返回值的任务，无法抛出异常
     */
    public static class CreateThreadByRunnable implements Runnable {

		public static void main(String[] args) {
			CreateThreadByRunnable createThreadByRunnable = new CreateThreadByRunnable();
			new Thread(createThreadByRunnable).start();
		}

		@Override
		public void run() {
			log.info("实现 Runnable 接口");
		}
	}

	/**
     *  使用 线程池创建线程
     *
     * <p>newCachedThreadPool：创建一个可根据需要创建新线程的线程池，调用 execute 将重用以前构造的线程（如果线程可
     * 用），如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
     *
     * <p>newFixedThreadPool：创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
     *
     * <p>newScheduledThreadPool：创建一个可调度的线程池。
     *
     * <p>newSingleThreadExecutor：返回一个只有一个线程的线程池，这个线程池可以在线程终结后，重新启动一个线程代理原来的线程继续执行。
     */
    public static class CreateThreadByThreadPool {

		public static void main(String[] args) {
			// 创建线程池
			ExecutorService threadPool = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 10; i++) {
				threadPool.execute(
					() -> {
						log.info("{} is running", Thread.currentThread().getName());
					});
			}
		}

		//		@Test
		//		public void testScheduledThreadPool() {
		//			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
		//
		//			// 延迟执行，（Runnable|Callable, delay(延迟时间)， unit(单位, TimeUnit)
		//			scheduledExecutorService.schedule(() -> System.out.println("延迟3秒"), 3, TimeUnit.SECONDS);
		//
		//			// 延迟固定执行，(initialDelay(延迟时间)，period(间隔时间), unit(单位, TimeUnit))
		//			scheduledExecutorService.scheduleAtFixedRate(
		//				() -> System.out.println("延迟1秒后，每 3 秒执行一次"), 1, 3, TimeUnit.SECONDS);
		//
		//			//			scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("22"), 1, 3,
		//			// TimeUnit.SECONDS);
		//		}
	}

	/**
     *  使用实现 Callable<T> 接口的方式创建线程，一般用于有返回值的任务，可以抛出异常
     *
     * <p>Callable 一般与 ExecutorService 一起使用
     */
    public static class CreateThreadByCallable implements Callable<Boolean> {

		public static void main(String[] args) throws ExecutionException, InterruptedException {
			// 通过 FutureTask 适配，FutureTask 继承 RunnableFuture，而 RunnableFuture 继承自 Runnable
			FutureTask futureTask = new FutureTask(new CreateThreadByCallable());
			new Thread(futureTask).start();
			// 结果会有缓存，且需要等待线程执行完成
			log.info("{}", futureTask.get());
		}

		@Override
		public Boolean call() throws Exception {
			return false;
		}
	}
}
