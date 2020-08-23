package me.hvkcoder.java_basic.jvm.reflect;

/**
 *
 * @author h-vk
 * @since 2020/8/23
 */
public class Robot {
	private String msg;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String hi() {
		return "Hi," + this.msg;
	}

	private String sayHello(String name) {
		return "Hello, " + name;
	}

}
