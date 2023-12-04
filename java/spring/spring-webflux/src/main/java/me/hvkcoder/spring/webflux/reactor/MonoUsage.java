package me.hvkcoder.spring.webflux.reactor;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Mono 返回 0-1 个数据
 *
 * @author h_vk
 * @since 2023/12/1
 */
@Slf4j
public class MonoUsage {
	public static void main(String[] args) {
		// 1、指定元素
		Mono<String> reactor = Mono.just("Reactor");
		reactor.subscribe(log::info);

		System.out.println();

		// 指定null元素
		Mono<Object> objectMono = Mono.justOrEmpty(Optional.empty());
		objectMono.subscribe(s -> log.info("{}", s));

		System.out.println();

	}
}
