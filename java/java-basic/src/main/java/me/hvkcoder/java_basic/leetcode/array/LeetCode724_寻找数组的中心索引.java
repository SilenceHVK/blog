package me.hvkcoder.java_basic.leetcode.array;

import java.util.Arrays;

/**
 * https://leetcode-cn.com/problems/find-pivot-index/
 *
 * <p>解法：对数组求和，并循环与 前缀和 对比差值，时间复杂度 O(1) 空间复杂度 O(1)
 *
 * <p>前缀和公式：leftSum + x + rightSum = sum x = sum - leftSum * 2
 *
 * @author h-vk
 * @since 2020/7/19
 */
public class LeetCode724_寻找数组的中心索引 {

	public static int pivotIndex(int[] nums) {
		int leftSum = 0;
		// 数组求和
		int sum = Arrays.stream(nums).sum();

		// 循环比较前缀和
		for (int i = 0; i < nums.length; i++) {
			if (leftSum * 2 + nums[i] == sum) return i;
			leftSum += nums[i];
		}
		return -1;
	}

	public static void main(String[] args) {
		int[] nums = {1, 7, 3, 6, 5, 6};
		int index = pivotIndex(nums);
		System.out.println(index);
	}
}
