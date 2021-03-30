package me.hvkcoder.java_basic.leetcode;

/**
 * https://leetcode-cn.com/problems/merge-two-sorted-lists/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode21_合并两个有序链表 {

	public static void main(String[] args) {
		ListNode l1 = new ListNode(1);
		l1.next = new ListNode(2);
		l1.next.next = new ListNode(4);
		l1.next.next.next = new ListNode(8);

		ListNode l2 = new ListNode(1);
		l2.next = new ListNode(3);
		l2.next.next = new ListNode(4);

		ListNode listNode = mergeTwoLists(l1, l2);
		while (listNode != null) {
			System.out.println(listNode.val);
			listNode = listNode.next;
		}
	}

	public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		ListNode sentinel = new ListNode(0);
		ListNode curr = sentinel;

		/** l1,l2 同为有序链表，因此排除中间会出现 null 值， 对 l1，l2 相同链表值进行遍历值比较 */
		while (l1 != null && l2 != null) {
			if (l1.val <= l2.val) {
				curr.next = new ListNode(l1.val);
				l1 = l1.next;
			} else {
				curr.next = new ListNode(l2.val);
				l2 = l2.next;
			}
			curr = curr.next;
		}

		/** 遍历后的链表， l1 或 l2 会出现非 null， 又因为是有序链表，因此直接追加 */
		curr.next = l1 == null ? l2 : l1;
		return sentinel.next;
	}

	public static class ListNode {
		int val;
		ListNode next;

		ListNode() {
		}

		ListNode(int val) {
			this.val = val;
		}

		ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
	}
}
