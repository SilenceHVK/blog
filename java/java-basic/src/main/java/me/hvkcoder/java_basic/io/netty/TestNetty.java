package me.hvkcoder.java_basic.io.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.InetSocketAddress;

/**
 * @author h_vk
 * @since 2021/7/18
 */
@Slf4j
public class TestNetty {

  @Test
  public void testServerMode() throws InterruptedException {
    final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final NioServerSocketChannel serverSocketChannel = new NioServerSocketChannel();
    eventLoopGroup.register(serverSocketChannel);
    serverSocketChannel
        .pipeline()
        .addLast(
            new AcceptHandler(
                eventLoopGroup,
                new InitChannel() {
                  @Override
                  public void initChannel(ChannelHandlerContext ctx) {
                    ctx.pipeline().addLast(new IOHandler());
                  }
                }));
    serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 9999)).sync();
    serverSocketChannel.closeFuture().sync();
    log.info("服务器已关闭");
  }

  @Test
  public void testClientMode() throws InterruptedException {
    final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final NioSocketChannel socketChannel = new NioSocketChannel();
    eventLoopGroup.register(socketChannel);
    socketChannel.pipeline().addLast(new IOHandler());
    socketChannel.connect(new InetSocketAddress("127.0.0.1", 9999)).sync();
    socketChannel.writeAndFlush(Unpooled.copiedBuffer("Hello World".getBytes()));
    socketChannel.closeFuture().sync();
    log.info("客户端已断开");
  }

  @Test
  public void testNettyServer() throws InterruptedException {
    final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final ServerBootstrap serverBootstrap = new ServerBootstrap();
    final ChannelFuture channelFuture =
        serverBootstrap
            .group(eventLoopGroup, eventLoopGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(
                new ChannelInitializer<NioSocketChannel>() {
                  @Override
                  protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new IOHandler());
                  }
                })
            .bind(new InetSocketAddress("127.0.0.1", 9999));
    channelFuture.sync().channel().closeFuture().sync();
  }

  @Test
  public void testNettyClient() throws InterruptedException {
    final NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
    final Bootstrap bootstrap = new Bootstrap();
    final ChannelFuture channelFuture =
        bootstrap
            .group(eventLoopGroup)
            .channel(NioSocketChannel.class)
            .handler(new IOHandler())
            .connect(new InetSocketAddress("127.0.0.1", 9999));

    final Channel channel = channelFuture.sync().channel();
    channel.writeAndFlush(Unpooled.copiedBuffer("Hello World".getBytes())).sync();
    channel.closeFuture().sync();
  }

  /** 处理客户端连接 */
  public class AcceptHandler extends ChannelInboundHandlerAdapter {
    private EventLoopGroup eventLoopGroup;
    private InitChannel channelHandler;

    public AcceptHandler(EventLoopGroup eventLoopGroup, InitChannel channelHandler) {
      this.eventLoopGroup = eventLoopGroup;
      this.channelHandler = channelHandler;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      log.info("服务器已启动，等待客户端连接......");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      final SocketChannel socketChannel = (SocketChannel) msg;
      log.info("客户端已连接->{}", socketChannel.remoteAddress());
      eventLoopGroup.register(socketChannel);
      socketChannel.pipeline().addLast(channelHandler);
    }
  }

  /** 中间过桥，用于用户 Handler 注册 */
  @ChannelHandler.Sharable
  public abstract class InitChannel extends ChannelInboundHandlerAdapter {
    public abstract void initChannel(ChannelHandlerContext ctx);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      initChannel(ctx);
      ctx.pipeline().remove(this);
    }
  }

  /** IO 处理 */
  public class IOHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
      log.info("channel register");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      log.info("channel active");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      final ByteBuf byteBuf = (ByteBuf) msg;
      final CharSequence charSequence =
          byteBuf.getCharSequence(0, byteBuf.readableBytes(), CharsetUtil.UTF_8);
      log.info("{}", charSequence);
      ctx.writeAndFlush(byteBuf);
    }
  }
}
