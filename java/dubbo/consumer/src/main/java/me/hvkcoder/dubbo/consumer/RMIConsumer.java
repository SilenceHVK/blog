package me.hvkcoder.dubbo.consumer;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.dubbo.common.service.GreetingsService;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * 使用 RMI 实现服务远程调用
 *
 * @author h_vk
 * @since 2021/8/11
 */
@Slf4j
public class RMIConsumer {
	public static void main(String[] args) throws RemoteException, NotBoundException {
		// 获取注册中心
		final Registry registry = LocateRegistry.getRegistry(9999);
		// 通过服务名获取远程实例
		final GreetingsService greeting = (GreetingsService) registry.lookup("greeting");
		log.info(greeting.sayHi("RMI"));
	}
}
