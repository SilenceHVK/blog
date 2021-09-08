package me.hvkcoder.spring.practice.sse;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Spring boot 整合 SSE
 *
 * @author h_vk
 * @since 2021/9/7
 */
@RestController
@RequestMapping("/sse")
public class SSEController {

	@CrossOrigin
	@GetMapping(value = "/log")
	public SseEmitter ssePractice(){
		final SseEmitter emitter = new SseEmitter(60 * 1000L);
		ThreadUtil.execute(()->{
			for (int i = 0; i < 10; i++) {
				try {
					emitter.send(LocalDateTimeUtil.format(LocalDateTimeUtil.now(), DatePattern.NORM_DATETIME_PATTERN));
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
					emitter.completeWithError(e);
				}
			}
			emitter.complete();
		});
		return emitter;
	}
}
