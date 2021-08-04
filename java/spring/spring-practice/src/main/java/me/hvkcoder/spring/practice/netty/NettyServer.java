package me.hvkcoder.spring.practice.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @author h_vk
 * @since 2021/7/22
 */
@Slf4j
@Component
public class NettyServer {
	private final EventLoopGroup bossGroup = new NioEventLoopGroup();
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private Channel channel;

	public ChannelFuture bind(InetSocketAddress inetSocketAddress) {
		ChannelFuture channelFuture = null;
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			channelFuture = serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<NioSocketChannel>() {
				@Override
				protected void initChannel(NioSocketChannel ch) throws Exception {
					ch.pipeline()
						.addLast(new LineBasedFrameDecoder(1024))
						.addLast(new StringDecoder())
						.addLast(new StringEncoder())
						.addLast(new ServerHandler());
				}
			}).bind(inetSocketAddress).sync();
			this.channel = channelFuture.channel();
		} catch (InterruptedException e) {
			e.printStackTrace();

		} finally{
			if (channelFuture != null && channelFuture.isSuccess()) {
				log.info("服务端启动成功，等待客户端连接......");
			}else {
				log.info("服务端启动错误......");
			}
		}
		return channelFuture;
	}

	public void destroy() {
		if (channel == null) return;
		channel.close();
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
	}

	public Channel getChannel() {
		return channel;
	}
}
