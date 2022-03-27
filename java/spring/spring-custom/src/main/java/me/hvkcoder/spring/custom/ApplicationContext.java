package me.hvkcoder.spring.custom;

/**
 * @author h_vk
 * @since 2022/3/26
 */
public interface ApplicationContext {
  Object getBean(String beanName) throws Exception;

  <T> T getBean(Class<T> beanType)throws Exception;
}
