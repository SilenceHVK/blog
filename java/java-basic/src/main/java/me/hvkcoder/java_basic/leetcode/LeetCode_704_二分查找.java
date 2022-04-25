package me.hvkcoder.java_basic.leetcode;

/**
 * https://leetcode-cn.com/problems/binary-search/
 *
 * @author h_vk
 * @since 2022/4/24
 */
public class LeetCode_704_二分查找 {
	public static void main(String[] args) {
		int[] nums = {-1, 0, 3, 5, 9, 12};
		System.out.println(search(nums, 12));
	}

	public static int search(int[] nums, int target) {
		int startIndex = 0, endIndex = nums.length - 1;
		while (startIndex <= endIndex) {
			int mid = startIndex + ((endIndex - startIndex) >> 1);
			int value = nums[mid];
			if (value == target) {
				return mid;
			} else if (value > target) {
				endIndex = mid - 1;
			} else {
				startIndex = mid + 1;
			}
		}
		return -1;
	}
}
