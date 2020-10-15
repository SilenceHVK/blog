package me.hvkcoder.java_basic.jvm.reflect;

import java.util.List;
import java.util.Map;

/**
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

	public Map<String, String> test(Map<String, String> map, List<String> list) {
		System.out.println("Hello Test");
		return null;
	}
}
