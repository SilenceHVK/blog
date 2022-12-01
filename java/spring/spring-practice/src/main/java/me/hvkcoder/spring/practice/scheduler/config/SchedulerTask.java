package me.hvkcoder.spring.practice.scheduler.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author h_vk
 * @since 2022/12/2
 */
@Slf4j
@Data
@Component
public class SchedulerTask implements SchedulingConfigurer {
	@Value("${scheduler.cron}")
	private String cron;

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.addTriggerTask(() -> log.info("Current DateTime: {}", LocalDateTime.now()), triggerContext -> {
			CronTrigger cronTrigger = new CronTrigger(cron);
			return cronTrigger.nextExecutionTime(triggerContext);
		});
	}
}
