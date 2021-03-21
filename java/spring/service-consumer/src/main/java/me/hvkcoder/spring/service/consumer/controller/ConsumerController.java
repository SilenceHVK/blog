package me.hvkcoder.spring.service.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author h-vk
 * @since 2021/3/18
 */
@RestController
public class ConsumerController {
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private RestTemplate restTemplate;


	@GetMapping("/get/services")
	public List<String> services() {
		List<String> services = discoveryClient.getServices();
		return services;
	}

	@GetMapping("/get/call-provider/{name}")
	public String callProvider(@PathVariable String name) {
		List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-PROVIDER");
		ServiceInstance serviceInstance = instances.get(0);
		String url = String.format("http://%s:%d/index/%s", serviceInstance.getHost(), serviceInstance.getPort(), name);
		return restTemplate.getForObject(url, String.class);
	}

}
