package me.hvkcoder.spring.custom;

/**
 * Bean 的定义类
 *
 * @author h_vk
 * @since 2022/3/27
 */
public class BeanDefinition {
  /** 设置 Bean Scope 常量 */
  public static final String SCOPE_SINGLETON = "singleton";

  public static final String SCOPE_PROTOTYPE = "prototype";

  private Class<?> beanClass;
  private String scope;

  public Class<?> getBeanClass() {
    return beanClass;
  }

  public void setBeanClass(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  public String getScope() {
    return scope;
  }

  public void setScope(String scope) {
    this.scope = scope;
  }
}
