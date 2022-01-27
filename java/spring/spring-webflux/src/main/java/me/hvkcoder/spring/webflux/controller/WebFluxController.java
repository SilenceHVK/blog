package me.hvkcoder.spring.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Reactor =  JDK8 的 Stream + JDK9 的 Reactive Stream
 *
 * @author h_vk
 * @since 2022/1/27
 */
@RestController
@Slf4j
public class WebFluxController {
	private String doSomething() {
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Hello World";
	}

	@RequestMapping("/normal")
	public String normal() {
		log.info("normal start");
		String result = doSomething();
		log.info("normal end");
		return result;
	}

	/**
	 * Mono 返回 0-1 个数据
	 *
	 * @return
	 */
	@RequestMapping("/mono")
	public Mono<String> mono() {
		log.info("mono start");
		Mono<String> result = Mono.fromSupplier(this::doSomething);
		log.info("mono end");
		return result;
	}

	/**
	 * Flux 返回 0-N 个数据， 需要执行 produces="text/event-stream" 以流的方式展示数据
	 *
	 * @return
	 */
	@RequestMapping(value = "/flux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> flux() {
		return Flux.fromStream(IntStream.range(1, 5).mapToObj(o -> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "flux data --- " + o;
		}));
	}
}
