package me.hvkcoder.spring.practice.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.spring.practice.scheduler.config.SchedulerTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h_vk
 * @since 2022/12/2
 */

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/scheduler")
public class SchedulerController {

	private SchedulerTask schedulerTask;

	
	@GetMapping("/update")
	public String updateCron(String cron) {
		log.info("new cron: {}", cron);
		schedulerTask.setCron(cron);
		return "ok";
	}
}
