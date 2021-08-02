package me.hvkcoder.java_basic.io.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * @author h_vk
 * @since 2021/7/22
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端已连接：{}", ctx.channel().remoteAddress());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端断开连接：{}", ctx.channel().remoteAddress());
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
		if (msg instanceof TextWebSocketFrame) {
			final String content = ((TextWebSocketFrame) msg).text();
			ctx.channel().writeAndFlush(new TextWebSocketFrame(content.toUpperCase()));
		} else {
			String message = "unsupported frame type: " + msg.getClass().getName();
			throw new UnsupportedOperationException(message);
		}
	}
}
