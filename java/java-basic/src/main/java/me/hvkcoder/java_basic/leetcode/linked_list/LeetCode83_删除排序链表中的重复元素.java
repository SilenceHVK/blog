package me.hvkcoder.java_basic.leetcode.linked_list;

/**
 * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-list/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode83_删除排序链表中的重复元素 {
	public static void main(String[] args) {
		ListNode head = new ListNode(1);
		head.next = new ListNode(1);
		head.next.next = new ListNode(2);

		ListNode listNode = deleteDuplicates(head);
		while (listNode != null) {
			System.out.println(listNode.val);
			listNode = listNode.next;
		}
	}

	public static ListNode deleteDuplicates(ListNode head) {
		ListNode curr = head;
		while (curr != null && curr.next != null) {
			if (curr.val == curr.next.val) {
				curr.next = curr.next.next;
			} else {
				curr = curr.next;
			}
		}
		return head;
	}

	public static class ListNode {
		int val;
		ListNode next;

		ListNode(int x) {
			val = x;
		}
	}
}
