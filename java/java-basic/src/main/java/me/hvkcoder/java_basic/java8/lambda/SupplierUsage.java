package me.hvkcoder.java_basic.java8.lambda;

import java.util.function.Supplier;

/**
 * java.util.function.Supplier<T> 接口定义了一个 get 的方法，返回一个指定的泛型
 *
 * @author h_vk
 * @since 2022/1/23
 */
public class SupplierUsage {

	public static void main(String[] args) {
		Supplier<SupplierUsage> supplier = SupplierUsage::new;
		System.out.println(supplier.get());
	}
}
