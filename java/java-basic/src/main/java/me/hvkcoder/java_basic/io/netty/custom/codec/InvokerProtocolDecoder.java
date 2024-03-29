package me.hvkcoder.java_basic.io.netty.custom.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.List;

/**
 * @author h_vk
 * @since 2024/3/29
 */
public class InvokerProtocolDecoder extends ByteToMessageDecoder {
	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
		if (byteBuf.readableBytes() < 4) return;
		byte[] data = new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(data);
		try (
			ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
		) {
			list.add(objectInputStream.readObject());
			byteBuf.skipBytes(byteBuf.readableBytes());
		}
	}
}
