package me.hvkcoder.java_basic.io.netty.echo.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * TODO: Netty echo客户端
 *
 * @author h-vk
 * @since 2021/1/12
 */
public class EchoClient {
	public static void main(String[] args) throws InterruptedException {
		// 创建 I/O 线程池
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 创建引导器
			Bootstrap bootstrap = new Bootstrap();

			bootstrap
				// 绑定 I/O 线程池
				.group(group)
				// 指定适用的 NIO 传输 Channel
				.channel(NioSocketChannel.class)
				// 指定服务端地址
				.remoteAddress(new InetSocketAddress("localhost", 9090))
				// 指定 Handler
				.handler(
					new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 添加自定义 Handler 到 Channel Pipeline 中
							ch.pipeline().addLast(new EchoClientHandler());
						}
					});

			// 连接到远程节点，sync() 表示阻塞等待直到连接完成
			ChannelFuture future = bootstrap.connect().sync();

			// 发送消息到远程节点
			future.channel().writeAndFlush(Unpooled.copiedBuffer("Hello World", CharsetUtil.UTF_8));

			// 关闭 Channel
			future.channel().closeFuture().sync();

		} finally {
			// 关闭线程池，并释放所有资源
			group.shutdownGracefully().sync();
		}
	}
}
