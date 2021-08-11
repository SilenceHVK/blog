package me.hvkcoder.dubbo.common.service.impl;

import me.hvkcoder.dubbo.common.service.GreetingsService;

/**
 * @author h_vk
 * @since 2021/8/11
 */
public class GreetingsServiceImpl implements GreetingsService {

	@Override
	public String sayHi(String name) {
		return "hi, " + name;
	}
}
