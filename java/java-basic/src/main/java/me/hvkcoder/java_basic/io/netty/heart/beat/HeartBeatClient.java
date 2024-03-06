package me.hvkcoder.java_basic.io.netty.heart.beat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Netty 心跳检测客户端
 *
 * @author h_vk
 * @since 2024/3/6
 */
@Slf4j
public class HeartBeatClient {
	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						channel.pipeline().addLast(new StringDecoder())
							.addLast(new StringEncoder())
							.addLast(new IdleStateHandler(0, 10, 0, TimeUnit.SECONDS))
							.addLast(new HeartBeatClientHandler());
					}
				});
			connect(bootstrap, "127.0.0.1", 9999);
		} finally {

		}
	}

	public static void connect(Bootstrap bootstrap, String host, Integer port) {
		ChannelFuture channelFuture = bootstrap.connect(host, port);
		channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				log.error("failed to connect to remote server, retrying connect to remote peer = {}:{}", host, port);
				channelFuture.channel().eventLoop().schedule(() -> connect(bootstrap, host, port), 3, TimeUnit.SECONDS);
			}
		});

		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					log.info("successfully connect to remote server, remote peer = {}:{}", host, port);
				}
			}
		});

	}

	public static class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
			if (evt instanceof IdleStateEvent idleStateEvent) {
				if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
					ctx.channel().writeAndFlush("PING");
				}
			}
			super.userEventTriggered(ctx, evt);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
			log.info("服务端 -> {}", s);
		}
	}

}
