package me.hvkcoder.dubbo.common.service.impl;

import me.hvkcoder.dubbo.common.service.GreetingsService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author h_vk
 * @since 2021/8/11
 */
@DubboService(version = "${spring.application.service.version}", timeout = 1000, interfaceClass = GreetingsService.class)
public class GreetingsServiceImpl implements GreetingsService {
	@Override
	public String sayHi(String name) {
		return "hi, " + name;
	}
}
