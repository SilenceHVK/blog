package me.hvkcoder.spring.test.service;

import me.hvkcoder.spring.custom.annotation.Autowired;
import me.hvkcoder.spring.custom.annotation.Component;
import me.hvkcoder.spring.custom.aware.BeanNameAware;
import me.hvkcoder.spring.custom.init.InitializingBean;

/**
 * @author h_vk
 * @since 2022/3/26
 */
@Component
public class UserService implements BeanNameAware, InitializingBean {
  @Autowired private OrderService orderService;

  private String beanName;

  public OrderService getOrderService() {
    return orderService;
  }

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  public String getBeanName() {
    return beanName;
  }

  @Override
  public void setBeanName(String name) {
    this.beanName = name;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    this.beanName = "H_VK";
  }
}
