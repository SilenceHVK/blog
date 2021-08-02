package me.hvkcoder.java_basic.io.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author h-vk
 * @since 2021/4/15
 */
public class WebSocketServer {
  public static void main(String[] args) throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

		try{
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(
				new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast(new HttpServerCodec());
						// 对写大数据流的的支持
						pipeline.addLast(new ChunkedWriteHandler());
						// 对 Http Message 进行聚合，聚合成一个 FullHttpRequest 或 FullHttpResponse
						pipeline.addLast(new HttpObjectAggregator(2014 * 6));
						//======================================== 以上是对 Http 的支持 ========================================

						// websocket 服务器处理的协议，用于指定客户端路由
						pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
						pipeline.addLast(new WebSocketHandler());
					}
				});

			ChannelFuture channelFuture = bootstrap.bind(8888).sync();
			channelFuture.channel().closeFuture().sync();
		}finally{
			bossGroup.shutdownGracefully().sync();
			workerGroup.shutdownGracefully().sync();
		}
  }
}
