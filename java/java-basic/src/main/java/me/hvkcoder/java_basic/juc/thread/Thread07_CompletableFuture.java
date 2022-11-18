package me.hvkcoder.java_basic.juc.thread;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author h_vk
 * @since 2022/11/17
 */
@Slf4j
public class Thread07_CompletableFuture {

	private CompletableFuture runAsyncCompletableFuture = null;
	private CompletableFuture<String> supplyAsyncCompletableFuture = null;

	@Before
	public void completableFuture() {
		// 有返回值的异步任务
		supplyAsyncCompletableFuture = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return LocalDateTimeUtil.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
		});

		// 无返回值的异步任务
		runAsyncCompletableFuture = CompletableFuture.runAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
				log.info("正在执行任务.....");
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		});
	}

	/**
	 * 获取结果数据
	 */
	@Test
	public void get() throws ExecutionException, InterruptedException {
		// get 方法可以设置超时时间，超时将抛出 TimeoutException
		//	log.info(supplyAsyncCompletableFuture.get());

		// 如果异步任务没有未计算完成，则使用 valueIfAbsent 作为结果值
		log.info(supplyAsyncCompletableFuture.getNow("默认值"));
	}

	/**
	 * 主动触发计算完成
	 */
	@Test
	public void complete() {
		// complete 是否打断 get 或 join 等待结果值方法，并返回默认值
		boolean completeValue = supplyAsyncCompletableFuture.complete("completeValue");
		log.info("{}\t{}", completeValue, supplyAsyncCompletableFuture.join());
	}

	/**
	 * 对结果数据进行处理
	 */
	@Test
	public void thenResultProcess() throws ExecutionException, InterruptedException {
		// thenApply 可以让上一步的结果作为下一步的参数进行传递，如果当前步骤出现异常，则任务不往下进行
//		String thenApplyResult = supplyAsyncCompletableFuture.thenApply(f -> f + ", 第二步分计算").thenApply(f -> f + ", 第三步分计算").get();
//		log.info("{}", thenApplyResult);

		// handle 与 thenApply 的功能是一样的，如果当前步骤出现异常，根据异常参数，传入下一步进行处理
		supplyAsyncCompletableFuture.handle((value, e) -> {
				log.info("执行第二步计算");
				return value + "， 第二步计算结果...";
			}).handle((value, e) -> {
				int i = 10 / 0;
				log.info("执行第三步计算");
				return value + "，第三步计算结果...";
			}).handle((value, e) -> {
				if (e != null) {
					log.error("{}", e.getMessage());
				}
				log.info("执行第四步计算");
				return value + "，第四步计算结果...";
			})
			.whenComplete((result, e) -> {
				if (e == null) {
					log.info("{}", result);
				}
			}).exceptionally(e -> {
				e.printStackTrace();
				log.error("{}", e.getMessage());
				return null;
			});
		TimeUnit.SECONDS.sleep(6);
	}

	/**
	 * 对结果数据进行消费
	 */
	@Test
	public void thenResultConsumer() throws InterruptedException {
		// thenAccept 接收任务结果，并消费结果，无返回值
		supplyAsyncCompletableFuture.thenAccept(result -> log.info("{}", result));
		TimeUnit.SECONDS.sleep(6);
	}

	/**
	 * 使用优先返回的异步任务结果
	 */
	@Test
	public void applyToEither() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return "This is result";
		});

		String result = supplyAsyncCompletableFuture.applyToEither(future, r -> "返回的结果 -> " + r).join();
		log.info("{}", result);
	}

	/**
	 * 对两个异步任务结果进行合并
	 */
	@Test
	public void thenCombine() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			return "Hello, World!";
		});

		String result = supplyAsyncCompletableFuture.thenCombine(future, (result1, result2) -> result1 + " - " + result2).join();
		log.info("{}", result);
	}


	/**
	 * 所有异步任务都执行完成后返回结果
	 */
	@Test
	public void allOf() {
		// 创建多个 CompletableFuture 任务
		CompletableFuture<String> priceTM = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("天猫"));
		CompletableFuture<String> priceTB = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("淘宝"));
		CompletableFuture<String> priceJD = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("京东"));

		// 将 CompletableFuture 任务放入集合，用户执行完成后，获取结果数据
		List<CompletableFuture<String>> prices = Arrays.asList(priceTM, priceTB, priceJD);
		CompletableFuture.allOf(prices.toArray(new CompletableFuture[0])).whenComplete((r, e) -> log.info("任务执行完成。。。。。")).join();

		prices.forEach(result -> log.info("{}", result.join()));
	}

	/**
	 * 任意异步任务执行完成后，获得其结果
	 */
	@Test
	public void anyOf() {
		// 创建多个 CompletableFuture 任务
		CompletableFuture<String> priceTM = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("天猫"));
		CompletableFuture<String> priceTB = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("淘宝"));
		CompletableFuture<String> priceJD = CompletableFuture.supplyAsync(() -> CreateThreadByCompletableFuture.price("京东"));

		Object result = CompletableFuture.anyOf(priceTM, priceTB, priceJD).whenComplete((r, e) -> log.info("任务执行完成。。。。。")).join();
		log.info("{}", result);
	}

	public static class CreateThreadByCompletableFuture {

		private static String price(String platform) {
			delay();
			return platform + "=> price: " + 5 + Math.random() * 20;
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
	}
}
