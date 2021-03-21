package me.hvkcoder.service.provider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h-vk
 * @since 2021/3/18
 */
@RestController
public class ProviderController {

	@GetMapping("/index/{name}")
	public String index(@PathVariable String name) {
		return "Hello " + name;
	}
}
