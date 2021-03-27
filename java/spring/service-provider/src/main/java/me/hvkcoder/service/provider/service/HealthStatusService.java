package me.hvkcoder.service.provider.service;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

/**
 * 手动设置服务上下线状态
 *
 * @author h-vk
 * @since 2021/3/21
 */
@Service
public class HealthStatusService implements HealthIndicator {

	private Boolean status = true;

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Boolean getStatus() {
		return status;
	}

	@Override
	public Health health() {
		if (status) {
			return new Health.Builder().up().build();
		} else {
			return new Health.Builder().down().build();
		}
	}
}
