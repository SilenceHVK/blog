package me.hvkcoder.spring.test.service;

import me.hvkcoder.spring.custom.ApplicationContext;
import me.hvkcoder.spring.custom.annotation.Autowired;
import me.hvkcoder.spring.custom.annotation.Component;
import me.hvkcoder.spring.custom.aware.ApplicationContextAware;

/**
 * @author h_vk
 * @since 2022/3/27
 */
@Component
public class OrderService implements ApplicationContextAware {
  @Autowired private UserService userService;

  private ApplicationContext applicationContext;

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }
}
