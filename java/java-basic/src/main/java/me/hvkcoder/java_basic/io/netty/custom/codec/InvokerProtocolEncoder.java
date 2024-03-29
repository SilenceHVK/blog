package me.hvkcoder.java_basic.io.netty.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 * @author h_vk
 * @since 2024/3/29
 */
public class InvokerProtocolEncoder extends MessageToByteEncoder<InvokerProtocol> {
	@Override
	protected void encode(ChannelHandlerContext channelHandlerContext, InvokerProtocol invokerProtocol, ByteBuf byteBuf) throws Exception {
		try (
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		) {
			objectOutputStream.writeObject(invokerProtocol);
			objectOutputStream.flush();
			byteBuf.writeBytes(byteArrayOutputStream.toByteArray());
		}
	}
}
