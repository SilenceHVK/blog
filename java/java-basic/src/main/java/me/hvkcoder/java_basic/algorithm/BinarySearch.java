package me.hvkcoder.java_basic.algorithm;

/**
 * 二分查找法
 *
 * @author h_vk
 * @since 2023/10/10
 */
public class BinarySearch {
	public static void main(String[] args) {
		int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9};
		System.out.println(binarySearch(numbers, 10));
		System.out.println(recursionBinarySearch(numbers, 5, 0, numbers.length - 1));
	}


	/**
	 * 二分查找法实现
	 *
	 * @param numbers
	 * @param target
	 * @return
	 */
	private static int binarySearch(int[] numbers, int target) {
		int startIndex = 0, endIndex = numbers.length - 1;
		while (startIndex <= endIndex) {
			int mid = startIndex + ((endIndex - startIndex) >> 1);
			int value = numbers[mid];
			if (value == target) {
				return mid;
			} else if (value < target) {
				startIndex = mid + 1;
			} else {
				endIndex = mid - 1;
			}
		}
		return -1;
	}

	/**
	 * 二分查找法递归
	 *
	 * @param numbers
	 * @param target
	 * @return
	 */
	private static int recursionBinarySearch(int[] numbers, int target, int startIndex, int endIndex) {
		if (startIndex >= endIndex) return -1;

		int mid = startIndex + endIndex >> 1;
		int value = numbers[mid];

		if (value == target) {
			return mid;
		} else if (value < target) {
			return recursionBinarySearch(numbers, target, mid + 1, endIndex);
		} else {
			return recursionBinarySearch(numbers, target, startIndex, mid - 1);
		}
	}
}
