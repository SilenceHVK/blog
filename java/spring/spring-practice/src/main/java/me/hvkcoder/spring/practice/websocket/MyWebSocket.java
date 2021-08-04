package me.hvkcoder.spring.practice.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author h_vk
 * @since 2021/7/20
 */
@Component
@ServerEndpoint(value = "/websocket/{ip}")
@Slf4j
public class MyWebSocket {

  /** 用于存放客户端对应的 Session */
  private static final ConcurrentHashMap<String, Session> webSocket = new ConcurrentHashMap<>();

  /**
   * 。连接建立成功调用该方法
   *
   * @param session
   * @param ip
   */
  @OnOpen
  public void onOpen(Session session, @PathParam("ip") String ip) {
    log.info("有客户端建立连接 {}", ip);
    webSocket.put(ip, session);
  }

  /**
   * 连接关闭调用的该方法
   *
   * @param ip
   */
  @OnClose
  public void onClose(@PathParam("ip") String ip) {
    log.info("客户端断开连接 {}", ip);
    webSocket.remove(ip);
  }

  /**
   * 收到客户端消息调用该方法
   *
   * @param message
   * @param session
   */
  @OnMessage
  public void onMessage(String message, Session session) {
    log.info("收到客户端消息 {}", message);
  }

  /**
   * 发生错误时调用
   *
   * @param session
   * @param e
   */
  @OnError
  public void onError(Session session, Throwable e) {
    log.error("错误异常, {}", e);
  }
}
