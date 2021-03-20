package me.hvkcoder.java_basic.juc.aqs;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 等待计数器归零，等待一个或多个线程执行完成后，再执行下面的方法，CountDownLatch 计数器使用完不能被重置
 *
 * @author h-vk
 * @since 2020/11/20
 */
@Slf4j
public class AQS01_CountDownLatch {
  public static void main(String[] args) throws InterruptedException {
    final int count = 60;
    final CountDownLatch countDownLatch = new CountDownLatch(count);
    ExecutorService executor = Executors.newCachedThreadPool();

    for (int i = 0; i < count; i++) {
      final int threadNum = i;
      executor.execute(
          () -> {
            try {
              TimeUnit.MILLISECONDS.sleep(100);
              log.info("{} go out", "Thread-" + threadNum);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } finally {
              countDownLatch.countDown();
            }
          });
    }
    countDownLatch.await();
    log.info("close door");
    executor.shutdown();
  }
}
