package me.hvkcoder.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import reactor.core.publisher.Flux;

import java.util.Arrays;

/**
 * 创建 Flux 序列，Flux 返回 0-N 个数据
 *
 * @author h_vk
 * @since 2023/12/1
 */
@Slf4j
public class FluxUsage {
	public static void main(String[] args) {
		// 1、直接返回流
		Flux<String> just = Flux.just("Java", "Golang", "Python");
		just.subscribe(log::info);

		System.out.println();

		// 2、从数据中创建流
		Flux<String> stringFlux = Flux.fromArray(new String[]{"Java", "Golang", "Python"});
		stringFlux.subscribe(log::info);

		System.out.println();

		// 3、从集合中创建流
		Flux<String> fluxIterable = Flux.fromIterable(Arrays.asList("Java", "Golang", "Python"));
		fluxIterable.subscribe(log::info);

		System.out.println();

		// 4、通过范围创建流
		Flux<Integer> range = Flux.range(1000, 5);
		range.subscribe(s -> log.info("{}", s));

		System.out.println();

		// 使用 form 工厂方法创建流
		Flux.from((Publisher<String>) s -> {
			for (int i = 0; i < 10; i++) {
				s.onNext("Hello " + i);
			}
			// 发送完成信号
			s.onComplete();
		}).subscribe(s -> log.info("{}", s), null, () -> log.info("处理结束"));

	}
}
