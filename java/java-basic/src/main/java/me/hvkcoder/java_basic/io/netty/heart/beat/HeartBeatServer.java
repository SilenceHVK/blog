package me.hvkcoder.java_basic.io.netty.heart.beat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Netty 心跳检测服务端
 *
 * @author h_vk
 * @since 2024/3/6
 */
@Slf4j
public class HeartBeatServer {
	public static void main(String[] args) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ChannelFuture channelFuture = new ServerBootstrap().group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						channel.pipeline().addLast(new StringEncoder())
							.addLast(new StringDecoder())
							.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS))
							.addLast(new HeartBeatServerHandler());
					}
				}).bind("127.0.0.1", 9999).sync();
			log.info("服务端已启动，等待客户端连接........");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	private static class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent idleStateEvent) {
				if (idleStateEvent.state() == IdleState.READER_IDLE) {
					ctx.channel().writeAndFlush("PONG");
				}
			}
			super.userEventTriggered(ctx, evt);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
			log.info("接收到客户端信息 -> {}", s);
		}
	}
}
