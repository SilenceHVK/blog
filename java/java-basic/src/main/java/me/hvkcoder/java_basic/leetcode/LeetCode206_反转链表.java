package me.hvkcoder.java_basic.leetcode;

/**
 * https://leetcode-cn.com/problems/reverse-linked-list/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode206_反转链表 {

	public static void main(String[] args) {
		ListNode root = new ListNode(1);
		root.next = new ListNode(2);
		root.next.next = new ListNode(3);
		root.next.next.next = new ListNode(4);
		root.next.next.next.next = new ListNode(5);

		ListNode result = reverseList(root);

		while (result != null) {
			System.out.println(result.val);
			result = result.next;
		}
	}

	/**
	 * 非递归的方式 将 next 指向上一个节点的引用地址
	 *
	 * @param head
	 * @return
	 */
	public static ListNode reverseList(ListNode head) {
		ListNode curr = head;
		ListNode tmp = null, prev = null;
		while (curr != null) {
			tmp = curr.next;
			curr.next = prev;
			prev = curr;
			curr = tmp;
		}
		return prev;
	}

	/**
	 * 递归方式
	 *
	 * @param head
	 * @return
	 */
	public static ListNode reverseListByRecursive(ListNode head) {
		if (head == null || head.next == null) {
			return head;
		}
		ListNode node = reverseListByRecursive(head.next);
		head.next.next = head;
		head.next = null;
		return node;
	}

	private static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
}
