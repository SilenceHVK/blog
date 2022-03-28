package me.hvkcoder.spring.custom.init;

/**
 * 执行 Bean 自定义初始化接口
 *
 * @author h_vk
 * @since 2022/3/28
 */
public interface InitializingBean {

  /**
   * 对已经填充完属性的 Bean 自定义设置属性值
   *
   * @throws Exception
   */
  void afterPropertiesSet() throws Exception;
}
