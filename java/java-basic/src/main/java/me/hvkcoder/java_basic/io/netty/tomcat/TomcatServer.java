package me.hvkcoder.java_basic.io.netty.tomcat;

import cn.hutool.core.thread.ThreadUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import me.hvkcoder.java_basic.io.netty.tomcat.http.TomcatRequest;
import me.hvkcoder.java_basic.io.netty.tomcat.http.TomcatResponse;
import me.hvkcoder.java_basic.io.netty.tomcat.http.TomcatServlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author h_vk
 * @since 2023/8/9
 */
@Slf4j
public class TomcatServer {

	private static final Map<String, TomcatServlet> servletMappings = new HashMap<>();

	public static void main(String[] args) throws IOException {
		Properties properties = new Properties();
		@Cleanup InputStream resourceAsStream = TomcatServer.class.getClassLoader().getResourceAsStream("web.properties");
		properties.load(resourceAsStream);

		properties.keySet().forEach(o -> {
			String key = o.toString();
			if (key.endsWith(".url")) {
				try {
					String keyPrefix = key.replaceAll("\\.url$", "");
					String url = properties.getProperty(key);
					String className = properties.getProperty(keyPrefix + ".className");
					TomcatServlet tomcatServlet = (TomcatServlet) Class.forName(className).getDeclaredConstructor().newInstance();
					servletMappings.put(url, tomcatServlet);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException |
								 ClassNotFoundException e) {
					throw new RuntimeException(e);
				}
			}
		});

//		tomcatServetSocket();
		tomcatNetty();
	}

	public static void tomcatServetSocket() throws IOException {
		@Cleanup ServerSocket serverSocket = new ServerSocket(9999);
		log.info("服务端已启动.....");

		while (true) {
			Socket client = serverSocket.accept();
			ThreadUtil.execute(() -> {
				try {
					@Cleanup InputStream inputStream = client.getInputStream();
					@Cleanup OutputStream outputStream = client.getOutputStream();

					TomcatRequest tomcatRequest = new TomcatRequest(inputStream);
					TomcatResponse tomcatResponse = new TomcatResponse(outputStream);

					String url = tomcatRequest.getUrl();
					if (servletMappings.containsKey(url)) {
						servletMappings.get(url).service(tomcatRequest, tomcatResponse);
					} else {
						tomcatResponse.write("404 NotFound");
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	public static void tomcatNetty() {
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						ch.pipeline().addLast(new HttpRequestDecoder())
							.addLast(new HttpResponseEncoder())
							.addLast(new TomcatServletHandler());
					}
				}).bind(9999).sync();
			log.info("服务端已启动......");
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static class TomcatServletHandler extends ChannelInboundHandlerAdapter {
		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			if (msg instanceof HttpRequest request) {
				TomcatRequest tomcatRequest = new TomcatRequest(ctx, request);
				TomcatResponse tomcatResponse = new TomcatResponse(ctx, request);
				String url = tomcatRequest.getUrl();
				if (servletMappings.containsKey(url)) {
					servletMappings.get(url).service(tomcatRequest, tomcatResponse);
				} else {
					tomcatResponse.write("<h1>404 NotFound</h1>");
				}
			}
		}
	}
}
