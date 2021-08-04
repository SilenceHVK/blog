package me.hvkcoder.spring.practice.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author h_vk
 * @since 2021/7/20
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {
  @Bean
  public ServerEndpointExporter serverEndpoint() {
    return new ServerEndpointExporter();
  }
}
