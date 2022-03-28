package me.hvkcoder.spring.custom.aware;

import me.hvkcoder.spring.custom.ApplicationContext;

/**
 * @author h_vk
 * @since 2022/3/28
 */
public interface ApplicationContextAware extends Aware {
  void setApplicationContext(ApplicationContext applicationContext);
}
