package me.hvkcoder.java_basic.desgin_patterns.behavioral;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 观察者模式：定义对象间的一种一对多依赖关系，使得当一个对象状态发生改变时，其相关依赖对象都会得到通知并更新
 *
 * @author h_vk
 * @since 2021/11/11
 */
public class ObserverPattern {
	@SneakyThrows
	public static void main(String[] args) {
		final Goods iPhone13 = new Goods();
		iPhone13.subscribe(new User("张三"));
		iPhone13.subscribe(new User("王五"));
		TimeUnit.MILLISECONDS.sleep(100);
		iPhone13.setInStock(true);
		iPhone13.notifyMessage();
	}

	/**
	 * 观察者接口
	 */
	private interface Observer {
		void pay();
	}

	/**
	 * 订阅者接口
	 */
	private interface Subscriber {
		/**
		 * 订阅方法
		 *
		 * @param observer
		 */
		void subscribe(Observer observer);

		/**
		 * 通知消息
		 */
		void notifyMessage();
	}

	/**
	 * 声明商品类实现订阅者接口，如果商品有现货则通知所有订阅的观察者
	 */
	@Data
	private static class Goods implements Subscriber {

		private final List<Observer> observers = new ArrayList<>();

		/**
		 * 是否有现货
		 */
		private boolean isInStock = false;

		@Override
		public void subscribe(Observer observer) {
			observers.add(observer);
		}

		@Override
		public void notifyMessage() {
			if (isInStock) {
				observers.forEach(Observer::pay);
			}
		}
	}

	/**
	 * 声明用户类实现观察者接口，收到订阅者消息后执行操作
	 */
	@AllArgsConstructor
	private static class User implements Observer {

		private String name;

		@Override
		public void pay() {
			System.out.println(name + " 开始付款");
		}
	}
}
