package me.hvkcoder.spring.custom.aware;

import me.hvkcoder.spring.custom.aware.Aware;

/**
 * @author h_vk
 * @since 2022/3/28
 */
public interface BeanNameAware extends Aware {
  void setBeanName(String name);
}
