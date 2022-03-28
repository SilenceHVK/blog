package me.hvkcoder.spring.custom.init;

/**
 * Bean 的后置处理器
 *
 * @author h_vk
 * @since 2022/3/28
 */
public interface BeanPostProcessor {

  /**
   * Bean 的前置处理器
   *
   * @param bean
   * @param beanName
   * @return
   */
  default Object postProcessorBeforInitialization(Object bean, String beanName) {
    return bean;
  }

  /**
   * Bean 的后置处理器
   *
   * @param bean
   * @param beanName
   * @return
   */
  default Object postProcessorAfterInitialization(Object bean, String beanName) {
    return bean;
  }
}
