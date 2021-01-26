package me.hvkcoder.java_basic.io.netty.echo.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 客户端 I/O 事件处理
 *
 * <p>ChannelHandler.Sharable: 表示一个 Channel Handler 可以被多个 Channel 安全共享
 *
 * <p>ByteBuf: 字节序列，通过 ByteBuf 操作基础的字节数组和缓冲区
 *
 * @author h-vk
 * @since 2021/1/12
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * 处理接收到消息
	 *
	 * @param ctx Channel 上下文
	 * @param msg Channel 字节序列
	 * @throws Exception
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		log.info("Client received: {} ", msg.toString(CharsetUtil.UTF_8));
	}

	/**
	 * 处理 Channel 中的异常
	 *
	 * @param ctx
	 * @param cause
	 * @throws Exception
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// 打印异常栈跟踪，并关闭 Channel
		cause.printStackTrace();
		ctx.close();
	}
}
