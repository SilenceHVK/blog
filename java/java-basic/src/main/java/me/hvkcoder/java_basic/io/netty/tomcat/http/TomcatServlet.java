package me.hvkcoder.java_basic.io.netty.tomcat.http;

/**
 * @author h_vk
 * @since 2023/8/9
 */
public abstract class TomcatServlet {

	public void service(TomcatRequest request, TomcatResponse response) throws Exception {
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			doGet(request, response);
		} else {
			doPost(request, response);
		}
	}

	public abstract void doGet(TomcatRequest request, TomcatResponse response) throws Exception;

	public abstract void doPost(TomcatRequest request, TomcatResponse response) throws Exception;
}
