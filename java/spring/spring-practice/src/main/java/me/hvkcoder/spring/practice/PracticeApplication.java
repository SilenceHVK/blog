package me.hvkcoder.spring.practice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;

/**
 * @author h_vk
 * @since 2021/7/20
 */
@Slf4j
@SpringBootApplication
@EnableScheduling // 启动 Spring 定时任务
public class PracticeApplication implements CommandLineRunner {
  @Value("${netty.host}")
  private String host;

  @Value("${netty.port}")
  private Integer port;

  public static void main(String[] args) {
    SpringApplication.run(PracticeApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    //		final ChannelFuture channelFuture = nettyServer.bind(new InetSocketAddress(host, port));
    //		Runtime.getRuntime().addShutdownHook(new Thread(()-> nettyServer.destroy()));
    //		channelFuture.channel().closeFuture().sync();
  }
}
