package me.hvkcoder.dubbo.common.service;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Java 的 RMI 规定接口必须派生 Remote ，并且每个方法必须抛出 RemoteException
 *
 * @author h_vk
 * @since 2021/8/11
 */
public interface GreetingsService extends Remote {
	String sayHi(String name) throws RemoteException;
}
