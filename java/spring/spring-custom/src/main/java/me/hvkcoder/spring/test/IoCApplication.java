package me.hvkcoder.spring.test;

import me.hvkcoder.spring.custom.AnnotationConfigApplicationContext;
import me.hvkcoder.spring.custom.ApplicationContext;
import me.hvkcoder.spring.custom.annotation.ComponentScan;
import me.hvkcoder.spring.test.service.UserService;

/**
 * @author h_vk
 * @since 2022/3/26
 */
@ComponentScan("me.hvkcoder.spring.test.service")
public class IoCApplication {
  public static void main(String[] args) {
		ApplicationContext applicationContext = new AnnotationConfigApplicationContext(IoCApplication.class);
		UserService userService = applicationContext.getBean(UserService.class);
	}
}
