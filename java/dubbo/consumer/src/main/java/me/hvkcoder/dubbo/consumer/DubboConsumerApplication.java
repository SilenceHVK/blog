package me.hvkcoder.dubbo.consumer;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot + Dubbo 实现服务消费者
 *
 * @author h_vk
 * @since 2021/8/11
 */
@DubboComponentScan
@SpringBootApplication
public class DubboConsumerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DubboConsumerApplication.class, args);
	}
}
