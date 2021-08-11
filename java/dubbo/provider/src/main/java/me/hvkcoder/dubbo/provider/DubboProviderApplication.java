package me.hvkcoder.dubbo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot + Dubbo 实现服务提供者
 *
 * @author h_vk
 * @since 2021/8/11
 */
@SpringBootApplication
public class DubboProviderApplication {
	public static void main(String[] args) {
		SpringApplication.run(DubboProviderApplication.class, args);
	}
}
