package me.hvkcoder.custom.spring.boot.starter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;

/**
 * @author h_vk
 * @since 2024/3/11
 */
@Slf4j
public class NettyServer implements CommandLineRunner, DisposableBean {
	private final EventLoopGroup bossGroup;
	private final EventLoopGroup workerGroup;

	private final NettyProperties nettyProperties;

	public NettyServer(NettyProperties nettyProperties) {
		this.nettyProperties = nettyProperties;
		this.bossGroup = new NioEventLoopGroup(1);
		this.workerGroup = new NioEventLoopGroup();
	}

	@Override
	public void destroy() throws Exception {
		log.info("正在关闭服务");
		this.workerGroup.shutdownGracefully();
		this.bossGroup.shutdownGracefully();
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("正在启动服务");
		ChannelFuture channelFuture = new ServerBootstrap()
			.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.childHandler(new ChannelInitializer<NioSocketChannel>() {
				@Override
				protected void initChannel(NioSocketChannel channel) throws Exception {

				}
			}).bind(nettyProperties.getHost(), nettyProperties.getPort()).sync();

		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (channelFuture.isSuccess()) {
					log.info("服务启动成功");
				} else {
					log.info("服务启动失败");
				}
			}
		});

	}
}
