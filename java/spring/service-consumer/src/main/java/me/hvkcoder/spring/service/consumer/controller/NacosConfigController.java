package me.hvkcoder.spring.service.consumer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author h_vk
 * @since 2021/6/17
 */
@RestController
@RefreshScope // 表示支持 Nacos 配置动态刷新
@RequestMapping("/nacos/config")
public class NacosConfigController {
  @Value("${name}")
  private String name;

  @GetMapping("/get/name")
  public String getConfigInfo() {
    return name;
  }
}
