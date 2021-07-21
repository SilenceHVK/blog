package me.hvkcoder.java_basic.io.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Netty 实现群聊
 *
 * @author h_vk
 * @since 2021/7/21
 */
@Slf4j
public class ChatNatty {
  public static void main(String[] args) {
    final NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    final NioEventLoopGroup workerGroup = new NioEventLoopGroup();
    try {
      final ServerBootstrap serverBootstrap = new ServerBootstrap();
      final ChannelFuture channelFuture =
          serverBootstrap
              .group(bossGroup, workerGroup)
              .channel(NioServerSocketChannel.class)
              .childHandler(
                  new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                      ch.pipeline()
                          .addLast(new StringEncoder())
                          .addLast(new StringDecoder())
                          .addLast(new ChatHandler());
                    }
                  })
              .bind(new InetSocketAddress("127.0.0.1", 9999))
              .sync();
      log.info("服务端已启动，等待客户端连接。。。。。。。");
      channelFuture.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
  }

  public static class ChatHandler extends SimpleChannelInboundHandler<String> {
    /** 存放在线用户信息 */
    private static final ConcurrentHashMap<NioSocketChannel, String> userMap =
        new ConcurrentHashMap<>();
    /**
     * 广播消息
     *
     * @param socketChannel
     * @param msg
     */
    private void broadcast(SocketChannel socketChannel, String msg) {
      for (SocketChannel channel : userMap.keySet()) {
        // 排除发送方
        if (channel != socketChannel) {
          channel.writeAndFlush(msg);
        }
      }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
      final NioSocketChannel socketChannel = (NioSocketChannel) ctx.channel();
      final String userId = userMap.get(socketChannel);
      String content = "【" + userId + "】：" + msg + "\n";
      log.info(content);
      broadcast(socketChannel, content);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      // 生成用户ID
      final String userId = "用户 " + ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
      final NioSocketChannel socketChannel = (NioSocketChannel) ctx.channel();
      String content = "【" + userId + "】已加入群聊\n";
      log.info(content);
      socketChannel.writeAndFlush("您的ID：" + userId + "\n");
      broadcast(socketChannel, content);
      userMap.put(socketChannel, userId);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
      final NioSocketChannel socketChannel = (NioSocketChannel) ctx.channel();
      final String userId = userMap.get(socketChannel);
      String content = "【" + userId + "】已退出群聊\n";
      log.info(content);
      broadcast(socketChannel, content);
      userMap.remove(socketChannel);
    }
  }
}
