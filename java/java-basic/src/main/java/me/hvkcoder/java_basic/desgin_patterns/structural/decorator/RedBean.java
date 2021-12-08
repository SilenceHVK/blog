package me.hvkcoder.java_basic.desgin_patterns.structural.decorator;

/**
 * 具体装饰
 *
 * @author h-vk
 * @since 2020/6/28
 */
public class RedBean extends Decorator {
	public RedBean(Drink drink) {
		super(drink);
	}

	@Override
	public double money() {
		return super.money() + 3.2;
	}

	@Override
	public String desc() {
		return super.desc() + "红豆";
	}
}
