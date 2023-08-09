package me.hvkcoder.java_basic.io.netty.tomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author h_vk
 * @since 2023/8/9
 */
public class TomcatResponse {

	private ChannelHandlerContext ctx;
	private HttpRequest request;
	private OutputStream outputStream;

	public TomcatResponse(OutputStream outputStream) {
		this.outputStream = outputStream;
	}


	public TomcatResponse(ChannelHandlerContext ctx, HttpRequest request) {
		this.ctx = ctx;
		this.request = request;
	}

	public void write(String s) throws IOException {
		if (outputStream != null) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("HTTP/1.1 200 OK\r\n")
				.append("Content-Type: text/plain\r\n")
				.append("\r\n")
				.append(s);
			this.outputStream.write(stringBuilder.toString().getBytes(CharsetUtil.UTF_8));
			this.outputStream.flush();
		} else {
			writeNetty(s);
		}
	}

	private void writeNetty(String s) {
		DefaultFullHttpResponse response = new DefaultFullHttpResponse(
			HttpVersion.HTTP_1_1,
			HttpResponseStatus.OK,
			Unpooled.wrappedBuffer(s.getBytes(CharsetUtil.UTF_8))
		);
		response.headers().add("Content-Type", "text/html");
		ctx.writeAndFlush(response);
	}
}
