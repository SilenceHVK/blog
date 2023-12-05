package me.hvkcoder.java_basic.leetcode;

/**
 * https://leetcode.cn/problems/climbing-stairs/
 * 解法1：斐波那契数列的变形
 *
 * @author h_vk
 * @since 2023/12/5
 */
public class LeetCode70_爬楼梯 {
	public static void main(String[] args) {
		int a = 0, b = 1;
		for (int i = 1; i <= 45; i++) {
			int tmp = a;
			a = b;
			b = tmp + b;
		}
		System.out.println(b);
	}
}
