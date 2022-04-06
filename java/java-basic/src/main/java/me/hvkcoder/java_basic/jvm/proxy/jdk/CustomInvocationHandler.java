package me.hvkcoder.java_basic.jvm.proxy.jdk;

import java.lang.reflect.Method;

/**
 * @author h_vk
 * @since 2022/4/2
 */
public interface CustomInvocationHandler {
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable;
}
