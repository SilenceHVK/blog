package me.hvkcoder.java_basic.io.netty.tomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author h_vk
 * @since 2023/8/9
 */
@Getter
public class TomcatRequest {

	private ChannelHandlerContext ctx;
	private HttpRequest request;

	private String method;
	private String url;

	public TomcatRequest(InputStream in) throws IOException {
		int len = 0;
		String content = "";
		byte[] buff = new byte[1024];
		if ((len = in.read(buff)) > 0) {
			content = new String(buff, 0, len);
		}
		String line = content.split("\\n")[0];
		String[] arr = line.split("\\s");
		this.method = arr[0];
		this.url = arr[1].split("\\?")[0];
	}

	public TomcatRequest(ChannelHandlerContext ctx, HttpRequest request) {
		this.ctx = ctx;
		this.request = request;
		this.url = request.uri();
		this.method = request.method().toString();
	}
}
