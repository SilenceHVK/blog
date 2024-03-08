package me.hvkcoder.java_basic.io.netty.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.java_basic.io.netty.protobuf.proto.MessageDataProto;

/**
 * @author h_vk
 * @since 2024/3/8
 */
@Slf4j
public class ProtoBufServer {
	public static void main(String[] args) {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup(1);
		try {
			ChannelFuture channelFuture = new ServerBootstrap().group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						channel.pipeline()
							.addLast(new ProtobufVarint32FrameDecoder())
							.addLast(new ProtobufDecoder(MessageDataProto.MessageData.getDefaultInstance()))
							.addLast(new ProtobufVarint32LengthFieldPrepender())
							.addLast(new ProtobufEncoder())
							.addLast(new ProtoBufServerHandler())
						;
					}
				}).bind(9999).sync();

			log.info("服务端已启动，等待客户端连接......");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	private static class ProtoBufServerHandler extends SimpleChannelInboundHandler<MessageDataProto.MessageData> {

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageDataProto.MessageData messageData) throws Exception {
			log.info("{}", messageData);
		}
	}
}
