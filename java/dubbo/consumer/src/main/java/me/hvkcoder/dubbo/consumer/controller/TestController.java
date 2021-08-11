package me.hvkcoder.dubbo.consumer.controller;

import me.hvkcoder.dubbo.common.service.GreetingsService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.rmi.RemoteException;

/**
 * @author h_vk
 * @since 2021/8/12
 */
@RestController
@RequestMapping("/test")
public class TestController {

	@DubboReference(version = "${spring.application.service.version}")
	private GreetingsService greetingsService;
	
	@GetMapping("/sayHi/{name}")
	public String sayHi(@PathVariable("name") String name) throws RemoteException {
		return greetingsService.sayHi(name);
	}
}
