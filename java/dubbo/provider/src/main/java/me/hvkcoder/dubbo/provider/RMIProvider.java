package me.hvkcoder.dubbo.provider;

import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.dubbo.common.service.GreetingsService;
import me.hvkcoder.dubbo.common.service.impl.GreetingsServiceImpl;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 使用 RMI 实现服务远程暴露
 *
 * @author h_vk
 * @since 2021/8/11
 */
@Slf4j
public class RMIProvider {
	public static void main(String[] args) throws RemoteException {
		// 实例要暴露的远程服务
		final GreetingsService greetingsService = new GreetingsServiceImpl();
		// 开启本地服务
		final GreetingsService exportObject = (GreetingsService) UnicastRemoteObject.exportObject(greetingsService, 6666);
		// 创建注册中心
		final Registry registry = LocateRegistry.createRegistry(9999);
		// 绑定远程服务
		registry.rebind("greeting", exportObject);
		log.info("service started...");
	}
}
