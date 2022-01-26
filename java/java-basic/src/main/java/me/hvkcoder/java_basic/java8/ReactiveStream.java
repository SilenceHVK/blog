package me.hvkcoder.java_basic.java8;

import lombok.SneakyThrows;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

/**
 * 响应式流操作
 *
 * @author h_vk
 * @since 2022/1/25
 */
public class ReactiveStream {
	public static void main(String[] args) throws InterruptedException {
		// 创建发布者， SubmissionPublisher 是 JDK9 内置的发布者，其内部实现了 Publisher 接口
		try (SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>()) {
			// 创建消息订阅者
			Flow.Subscriber<Integer> subscriber = new Flow.Subscriber<>() {
				private Flow.Subscription subscription;

				/**
				 * 在建立订阅关系时调用
				 * @param subscription
				 */
				@Override
				public void onSubscribe(Flow.Subscription subscription) {
					this.subscription = subscription;
					// 请求一个数据
					this.subscription.request(1);
				}

				/**
				 * 当接收到数据时调用
				 * @param item
				 */
				@SneakyThrows
				@Override
				public void onNext(Integer item) {
					System.out.println("接收数据 => " + item);
					TimeUnit.SECONDS.sleep(3);
					// 处理完数据后，可调用 request 在此请求数据
					this.subscription.request(1);
					// 或者调用 cancel 告诉发布者不再接收数据
					// this.subscription.cancel();
				}

				/**
				 * 在处理数据时出现异常时调用
				 * @param throwable
				 */
				@Override
				public void onError(Throwable throwable) {
					this.subscription.cancel();
				}

				/**
				 * 在结束接收数据时调用
				 */
				@Override
				public void onComplete() {
					System.out.println("完成数据处理");
				}
			};
			// 将发布者与订阅者进行关联
			publisher.subscribe(subscriber);
			for (int i = 0; i < 1000; i++) {
				System.out.println("生产数据 => " + i);
				// 发布者发布数据，submit 是一个阻塞方法，当缓冲池被占满时，将被阻塞，缓冲池最大长度为 256
				publisher.submit(i);
			}
		}
		Thread.currentThread().join(10000);
	}
}
