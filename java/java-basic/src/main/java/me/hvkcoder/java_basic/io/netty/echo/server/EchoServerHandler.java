package me.hvkcoder.java_basic.io.netty.echo.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 服务端 I/O 事件处理
 *
 * @author h-vk
 * @since 2021/1/12
 */
@Slf4j
@ChannelHandler.Sharable // 表示一个 Channel Handler 可以被多个 Channel 安全的共享
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 读取 Channel 中的消息
	 *
	 * @param ctx
	 * @param msg
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf in = (ByteBuf) msg;
		log.info("Server received: {} ", in.toString(CharsetUtil.UTF_8));
		// 将接收到的消息发给发送者
		ctx.write(Unpooled.copiedBuffer("Server have received your message", CharsetUtil.UTF_8));
	}

	/**
	 * 读取 Channel 中的消息完成
	 *
	 * @param ctx
	 * @throws Exception
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// 清空 Channel 中的消息，并关闭该 Channel
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
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
