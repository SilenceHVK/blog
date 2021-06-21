package me.hvkcoder.spring.service.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author h-vk
 * @since 2021/3/18
 */
@RestController
@RequestMapping("/rest")
public class RestTemplateController {
	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	@Qualifier("loadBalancer")
	private RestTemplate loadBalancer;


	@GetMapping("/get/services")
	public List<String> services() {
		List<String> services = discoveryClient.getServices();
		return services;
	}

	@GetMapping("/get/call-provider/{name}")
	public String callProvider(@PathVariable String name) {
		List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-PROVIDER");
		ServiceInstance serviceInstance = instances.get(0);
		return callProvider(serviceInstance, name);
	}


	/**
	 * 用于记录请求次数
	 */
	private final AtomicInteger count = new AtomicInteger(0);

	/**
	 * 采用轮询的方式实现负载均衡
	 *
	 * @param name
	 * @return
	 */
	@GetMapping("/get/my-load-balance/{name}")
	public String callProviderByMyLoadBalance(@PathVariable String name) {
		List<ServiceInstance> instances = discoveryClient.getInstances("SERVICE-PROVIDER");
		ServiceInstance serviceInstance = instances.get(count.incrementAndGet() % instances.size());
		return callProvider(serviceInstance, name);
	}

	/**
	 * 通过 RestTemplate 提供的负载均衡请求
	 *
	 * @param name
	 * @return
	 */
	@GetMapping("/get/load-balance/{name}")
	public String callProviderByLoadBalance(@PathVariable String name) {
		return loadBalancer.getForObject("http://service-provider/index/${1}", String.class, name);
	}


	private String callProvider(ServiceInstance serviceInstance, String name) {
		String url = String.format("http://%s:%d/index/%s", serviceInstance.getHost(), serviceInstance.getPort(), name);
		return restTemplate.getForObject(url, String.class);
	}
}
