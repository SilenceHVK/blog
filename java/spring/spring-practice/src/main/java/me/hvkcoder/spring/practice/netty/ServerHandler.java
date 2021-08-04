package me.hvkcoder.spring.practice.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author h_vk
 * @since 2021/7/22
 */
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<String> {
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端已连接：{}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("客户端已断开：{}", ctx.channel().remoteAddress());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
		log.info("客户端消息：{}", msg);
		ctx.channel().writeAndFlush(msg);
	}
}
