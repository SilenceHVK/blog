package me.hvkcoder.java_basic.desgin_patterns.structural.decorator;

/**
 * 被装饰者
 *
 * @author h-vk
 * @since 2020/6/28
 */
public class Soya implements Drink {
	@Override
	public double money() {
		return 5;
	}

	@Override
	public String desc() {
		return "纯豆浆";
	}
}
