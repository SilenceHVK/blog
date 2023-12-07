package me.hvkcoder.java_basic.leetcode;

/**
 * https://leetcode.cn/problems/fibonacci-number/
 *
 * @author h_vk
 * @since 2023/12/6
 */
public class LeetCode509_斐波那契数 {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.println(fibSolution1(i));
		}

		System.out.println();
		
		for (int i = 0; i < 10; i++) {
			System.out.println(fibSolution2(i));
		}
	}

	public static int fibSolution1(int n) {
		int a = 0, b = 1;
		for (int i = 0; i < n; i++) {
			int tmp = a;
			a = b;
			b = tmp + b;
		}
		return a;
	}

	public static int fibSolution2(int n) {
		if (n <= 0) return 0;
		if (n < 2) return 1;
		return fibSolution2(n - 1) + fibSolution2(n - 2);
	}
}
