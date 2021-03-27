package me.hvkcoder.service.provider.controller;

import me.hvkcoder.service.provider.service.HealthStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h-vk
 * @since 2021/3/18
 */
@RestController
public class ProviderController {

	@Value("${server.port}")
	private int port;

	@Autowired
	private HealthStatusService healthStatusService;

	/**
	 * 设置服务健康状态
	 *
	 * @param health
	 * @return
	 */
	@PutMapping("/put/set-health/{health}")
	public String setHealth(@PathVariable Boolean health) {
		healthStatusService.setStatus(health);
		return healthStatusService.getStatus().toString();
	}

	@GetMapping("/index/{name}")
	public String index(@PathVariable String name) {
		return "Hello " + name + "， Port = " + port;
	}

}
