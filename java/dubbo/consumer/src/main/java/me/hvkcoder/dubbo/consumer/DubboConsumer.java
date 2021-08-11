package me.hvkcoder.dubbo.consumer;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.dubbo.common.service.GreetingsService;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;

import java.rmi.RemoteException;

/**
 * Dubbo 原生API 实现 服务消费者
 *
 * @author h_vk
 * @since 2021/8/11
 */
@Slf4j
public class DubboConsumer {
	public static void main(String[] args) throws RemoteException {
		// 实例 ReferenceConfig 实例
		final ReferenceConfig<GreetingsService> referenceConfig = new ReferenceConfig<>();
		// 设置应用程序信息
		referenceConfig.setApplication(new ApplicationConfig("dubbo-consumer"));
		// 设置注册中心
		referenceConfig.setRegistry(new RegistryConfig("zookeeper://k8s-180:30181/dubbo"));
		// 设置服务接口与超时时间
		referenceConfig.setInterface(GreetingsService.class);
		referenceConfig.setTimeout(5000);
		// 设置服务分组与版本
		referenceConfig.setGroup("dubbo");
		referenceConfig.setVersion("1.0.0");
		// 应用服务，并调用方法
		final GreetingsService service = referenceConfig.get();
		log.info(service.sayHi("Dubbo"));
	}
}
