package me.hvkcoder.spring.test.service;

import me.hvkcoder.spring.custom.annotation.Autowired;
import me.hvkcoder.spring.custom.annotation.Component;

/**
 * @author h_vk
 * @since 2022/3/27
 */
@Component
public class OrderService {
  @Autowired private UserService userService;

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }
}
