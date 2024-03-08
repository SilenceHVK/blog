package me.hvkcoder.java_basic.io.netty.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import me.hvkcoder.java_basic.io.netty.protobuf.proto.MessageDataProto;

/**
 * @author h_vk
 * @since 2024/3/8
 */
public class ProtoBufClient {
	public static void main(String[] args) {
		NioEventLoopGroup group = new NioEventLoopGroup();
		try {
			ChannelFuture channelFuture = new Bootstrap().group(group).channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel channel) throws Exception {
						channel.pipeline()
							.addLast(new ProtobufVarint32FrameDecoder())
							.addLast(new ProtobufDecoder(MessageDataProto.MessageData.getDefaultInstance()))
							.addLast(new ProtobufVarint32LengthFieldPrepender())
							.addLast(new ProtobufEncoder())
							.addLast(new ProtoBufClientHandler())
						;
					}
				}).connect("127.0.0.1", 9999).sync();

			channelFuture.channel().closeFuture().addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture channelFuture) throws Exception {
					channelFuture.channel().closeFuture();
				}
			});
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			group.shutdownGracefully();
		}
	}

	private static class ProtoBufClientHandler extends SimpleChannelInboundHandler<MessageDataProto.MessageData> {
		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			MessageDataProto.MessageData messageData = MessageDataProto.MessageData.newBuilder().setDataType(MessageDataProto.MessageData.DataType.PersonType)
				.setPerson(MessageDataProto.Person.newBuilder().setName("H_VK").setId(1234).build())
				.build();
			ctx.writeAndFlush(messageData);
		}

		@Override
		protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageDataProto.MessageData messageData) throws Exception {

		}
	}
}
