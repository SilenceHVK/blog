package me.hvkcoder.java_basic.juc.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Random;
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
	 * 使用继承 Thread 类的方式创建线程
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
	 * 使用实现 Runnable 接口的方式创建线程，一般用于无返回值的任务，无法抛出异常
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
	 * 使用 线程池创建线程
	 * 线程池主要用于管理线程，减少每个每个线程创建和销毁的性能开销
	 * newCachedThreadPool：创建一个可根据需要创建新线程的线程池，调用 execute 将重用以前构造的线程（如果线程可
	 * 用），如果现有线程没有可用的，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程。
	 * newFixedThreadPool：创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
	 * newScheduledThreadPool：创建一个可调度的线程池。
	 * newSingleThreadExecutor：返回一个只有一个线程的线程池，这个线程池可以在线程终结后，重新启动一个线程代理原来的线程继续执行。
	 * ThreadPoolExecutor：手动创建线程池 【推荐】
	 */
	public static class CreateThreadByThreadPool {


		/**
		 * 创建固定线程数线程池
		 */
		@Test
		public void testFixedThreadPool() {
			ExecutorService threadPool = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 20; i++) {
				threadPool.execute(
					() -> {
						System.out.println(Thread.currentThread().getName() + " is running");
					});
			}
		}

		/**
		 * 创建可调度的线程池
		 */
		@Test
		public void testScheduledThreadPool() {
			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
			// 延迟执行，（Runnable|Callable, delay(延迟时间)， unit(单位, TimeUnit)
			scheduledExecutorService.schedule(() -> log.info("延迟 3 秒"), 3, TimeUnit.SECONDS);
			// 延迟间隔执行， (initialDelay(延迟时间)，period(间隔时间), unit(单位, TimeUnit))
			scheduledExecutorService.scheduleAtFixedRate(
				() -> System.out.println("延迟1秒后，每 3 秒执行一次"), 1, 3, TimeUnit.SECONDS);
		}


		/**
		 * 自定义线程池
		 * int corePoolSize：线程池的基本大小
		 * int maximumPoolSize：线程池最大线程大小
		 * long keepAliveTime：线程空闲后的存活时间
		 * TimeUnit unit：线程空闲后的存活时间单位
		 * BlockingQueue<Runnable> workQueue：任务阻塞队列
		 * ThreadFactory threadFactory：线程工厂
		 * RejectedExecutionHandler handler：队列和最大线程池都满了后的拒绝策略
		 * RejectedExecutionHandler JDK 默认提供了 4 种策略
		 * - Abort 抛异常
		 * - Discard 扔掉，不抛异常
		 * - DiscardOldest 扔掉排队时间最久的
		 * - CallerRuns 调用者处理任务
		 */
		@Test
		public void testThreadPoolExecutor() {
			ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
				2,
				4,
				60,
				TimeUnit.SECONDS,
				new ArrayBlockingQueue<Runnable>(4),
				Executors.defaultThreadFactory(),
				new ThreadPoolExecutor.CallerRunsPolicy()
			);

			for (int i = 0; i < 20; i++) {
				threadPoolExecutor.execute(() -> System.out.println(Thread.currentThread().getName() + " is Running"));
			}
			threadPoolExecutor.shutdown();
		}
	}

	/**
	 * 使用实现 Callable<T> 接口的方式创建线程，一般用于有返回值的任务，可以抛出异常
	 */
	public static class CreateThreadByCallable implements Callable<Boolean> {

		/**
		 * 使用线程池执行 Callable
		 *
		 * @throws ExecutionException
		 * @throws InterruptedException
		 */
		@Test
		public void testThreadPoolExecutorCallable() throws ExecutionException, InterruptedException {
			ExecutorService service = Executors.newFixedThreadPool(5);
			Future<Boolean> future = service.submit(new CreateThreadByCallable());
			log.info("执行结果：{}", future.get());
			log.info("是否执行完成:{}", future.isDone());
		}

		/**
		 * FutureTask 的使用
		 *
		 * @throws ExecutionException
		 * @throws InterruptedException
		 */
		@Test
		public void testFutureTask() throws ExecutionException, InterruptedException {
			// 通过 FutureTask 适配，FutureTask 继承 RunnableFuture，而 RunnableFuture 继承自 Runnable 和 Future
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

	/**
	 * CompletableFuture：对多个任务进行管理
	 */
	public static class CreateThreadByCompletableFuture {

		private static Double price() {
			delay();
			return 5 + Math.random() * 20;
		}

		private static void delay() {
			int time = new Random().nextInt(500);
			try {
				TimeUnit.MILLISECONDS.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info("After {} sleep!", time);
		}

		/**
		 * CompletableFuture 并行执行
		 *
		 * @throws InterruptedException
		 */
		@Test
		public void testParallelExecution() throws InterruptedException {
			// 创建多个 CompletableFuture 任务
			CompletableFuture<Double> priceTM = CompletableFuture.supplyAsync(CreateThreadByCompletableFuture::price);
			CompletableFuture<Double> priceTB = CompletableFuture.supplyAsync(CreateThreadByCompletableFuture::price);
			CompletableFuture<Double> priceJD = CompletableFuture.supplyAsync(CreateThreadByCompletableFuture::price);

			// 任意一个 CompletableFuture 任务执行成功，则成功
			// CompletableFuture<Object> result = CompletableFuture.anyOf(priceTM, priceTB, priceJD);

			// 所有 CompletableFuture 任务执行成功，则成功
			CompletableFuture<Void> result = CompletableFuture.allOf(priceTM, priceTB, priceJD);

			// 执行成功
			result.thenAccept(System.out::println);
			TimeUnit.MINUTES.sleep(1);
		}

		/**
		 * CompletableFuture 串行执行
		 *
		 * @throws InterruptedException
		 */
		@Test
		public void testSerialExecution() throws InterruptedException {
			// 创建单个异步执行任务
			CompletableFuture<String> platformFuture =
				CompletableFuture.supplyAsync(
					() -> {
						delay();
						return "天猫";
					});

			// 串行执行，根据上一个 CompletableFuture 的结果去执行下一个 CompletableFuture
			CompletableFuture<String> resultFuture =
				platformFuture.thenApplyAsync(
					platFormName -> {
						delay();
						return platFormName + " 售价: " + (5 + Math.random() * 20);
					});

			// 执行成功
			resultFuture.thenAccept(System.out::println);

			// 执行异常
			resultFuture.exceptionally(
				e -> {
					e.printStackTrace();
					return null;
				});

			TimeUnit.MINUTES.sleep(1);
		}
	}
}
