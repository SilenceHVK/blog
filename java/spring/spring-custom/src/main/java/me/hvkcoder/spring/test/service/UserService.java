package me.hvkcoder.spring.test.service;

import me.hvkcoder.spring.custom.annotation.Autowired;
import me.hvkcoder.spring.custom.annotation.Component;

/**
 * @author h_vk
 * @since 2022/3/26
 */
@Component
public class UserService {
  @Autowired private OrderService orderService;

  public OrderService getOrderService() {
    return orderService;
  }

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }
}
