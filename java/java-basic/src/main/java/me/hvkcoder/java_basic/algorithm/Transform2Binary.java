package me.hvkcoder.java_basic.algorithm;

/**
 * 转换为二进制
 *
 * @author h_vk
 * @since 2022/4/25
 */
public class Transform2Binary {
	private static void printBinary(int num) {
		for (int i = 31; i >= 0; i--) {
			System.out.print((num & (1 << i)) == 0 ? "0" : "1");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int num = Integer.MAX_VALUE;
		printBinary(num);
	}
}
