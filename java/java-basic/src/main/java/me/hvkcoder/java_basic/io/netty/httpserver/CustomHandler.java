package me.hvkcoder.java_basic.io.netty.httpserver;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

/**
 * @author h-vk
 * @since 2021/4/15
 */
public class CustomHandler extends SimpleChannelInboundHandler<HttpObject> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
    Channel channel = ctx.channel();
    if (msg instanceof HttpRequest) {
      System.out.println("客户端 -> " + channel.remoteAddress());
      ByteBuf content = Unpooled.copiedBuffer("Hello Netty", CharsetUtil.UTF_8);
      DefaultFullHttpResponse response =
          new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
      response
          .headers()
          .set(HttpHeaderNames.CONTENT_TYPE, "text/plain")
          .set(HttpHeaderNames.CONTENT_LENGTH, content.readableBytes());
      channel.writeAndFlush(response);
    }
  }
}
