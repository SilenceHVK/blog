package me.hvkcoder.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.dubbo.common.service.GreetingsService;
import me.hvkcoder.dubbo.common.service.impl.GreetingsServiceImpl;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.ServiceConfig;

import java.util.concurrent.CountDownLatch;

/**
 * Dubbo 原生API 实现 服务提供者
 *
 * @author h_vk
 * @since 2021/8/11
 */
@Slf4j
public class DubboProvider {
	public static void main(String[] args) throws InterruptedException {
		// 创建 ServiceConfig 实例
		final ServiceConfig<GreetingsService> serviceConfig = new ServiceConfig<>();
		// 设置应用程序配置
		serviceConfig.setApplication(new ApplicationConfig("dubbo-provider"));
		// 设置服务注册中心
		serviceConfig.setRegistry(new RegistryConfig("zookeeper://k8s-180:30181/dubbo"));
		// 设置接口与实现类
		serviceConfig.setInterface(GreetingsService.class);
		serviceConfig.setRef(new GreetingsServiceImpl());
		// 设置服务分组与版本
		serviceConfig.setVersion("1.0.0");
		serviceConfig.setGroup("dubbo");
		// 导出服务
		serviceConfig.export();
		log.info("dubbo service started...");
		new CountDownLatch(1).await();
	}
}
