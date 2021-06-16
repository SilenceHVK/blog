package me.hvkcoder.spring_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author h_vk
 * @since 2021/6/15
 */
@Controller
public class PageController {
  @GetMapping({"/login", "/login.html", "/login.htm"})
  public String login() {
    return "login";
  }
}
