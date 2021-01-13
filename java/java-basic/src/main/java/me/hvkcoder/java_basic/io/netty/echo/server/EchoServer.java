package me.hvkcoder.java_basic.io.netty.echo.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

/**
 * TODO: Netty echo服务端
 *
 * @author h-vk
 * @since 2021/1/12
 */
public class EchoServer {

	public static void main(String[] args) throws InterruptedException {
		// 创建 I/O 线程池
		EventLoopGroup group = new NioEventLoopGroup();

		try {
			// 创建辅助工具类，用于服务器通道的一系列配置
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 绑定线程组
			bootstrap
				.group(group)
				// 指定适用的 NIO 传输 Channel
				.channel(NioServerSocketChannel.class)
				// 指定 Socket 端口号
				.localAddress(new InetSocketAddress(9090))
				// 指定 Handler
				.childHandler(
					new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoServerHandler());
						}
					});

			// 异步绑定服务器，sync() 方法表示阻塞等待直到绑定完成
			ChannelFuture future = bootstrap.bind().sync();
			// 关闭 Channel Future
			future.channel().closeFuture().sync();
		} finally {
			// 关闭 EventLoopGroup，释放所有的资源
			group.shutdownGracefully().sync();
		}
	}
}
