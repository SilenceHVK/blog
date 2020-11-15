package me.hvkcoder.java_basic.leetcode.array;

/**
 * TODO: https://leetcode-cn.com/problems/search-insert-position/
 * <p>
 * 解法：使用二分查找法，时间复杂度O(Nlog) 空间复杂度O(1)
 *
 * @author h-vk
 * @since 2020/7/19
 */
public class LeetCode35_搜索插入位置 {
	public static int searchInsert(int[] nums, int target) {
		int startIndex = 0, endIndex = nums.length - 1;

		// 如果第一个元素大于 target 直接返回  0
		if (nums[0] > target) return 0;

		// 实现二分查找法
		while (startIndex <= endIndex) {
			int mid = startIndex + endIndex >> 1;
			if (nums[mid] == target) return mid;
			else if(nums[mid] > target) endIndex = mid - 1;
			else startIndex = mid + 1;
		}

    // 如果 target 在数组中不存在，则返回其应该插入的位置
		return endIndex + 1;
	}

  public static void main(String[] args) {
		int[] nums = {1,3,5,6};
		int target = 4;
		int index = searchInsert(nums, target);
    System.out.println(index);
	}
}
