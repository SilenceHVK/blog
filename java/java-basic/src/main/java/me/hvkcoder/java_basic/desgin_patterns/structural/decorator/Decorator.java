package me.hvkcoder.java_basic.desgin_patterns.structural.decorator;

/**
 * 装饰器
 *
 * @author h-vk
 * @since 2020/6/28
 */
public abstract class Decorator implements Drink {
	private Drink drink;

	public Decorator(Drink drink) {
		this.drink = drink;
	}

	@Override
	public double money() {
		return drink.money();
	}

	@Override
	public String desc() {
		return drink.desc();
	}
}
