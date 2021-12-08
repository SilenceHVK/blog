package me.hvkcoder.java_basic.desgin_patterns.structural.decorator;

/**
 * @author h-vk
 * @since 2020/6/28
 */
public class Main {
	public static void main(String[] args) {
		Drink redBeanSoya = new RedBean(new Soya());
		System.out.println(redBeanSoya.desc());
		System.out.println(redBeanSoya.money());
	}
}
