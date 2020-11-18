package me.hvkcoder.java_basic.leetcode.linked_list;

/**
 * TODO: https://leetcode-cn.com/problems/add-two-numbers/
 *
 * @author h-vk
 * @since 2020/11/17
 */
public class LeetCode2_两数相加 {
	public static void main(String[] args) {
		ListNode l1 = new ListNode(2);
		l1.next = new ListNode(4);
		l1.next.next = new ListNode(3);

		ListNode l2 = new ListNode(5);
		l2.next = new ListNode(6);
		l2.next.next = new ListNode(4);

		ListNode listNode = addTwoNumbers(l1, l2);
		while (listNode != null) {
			System.out.println(listNode.val);
			listNode = listNode.next;
		}
	}

	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode sentinel = new ListNode(0);
		ListNode curr = sentinel;

		int carry = 0;

		while (l1 != null || l2 != null) {

			int num1 = l1 == null ? 0 : l1.val;
			int num2 = l2 == null ? 0 : l2.val;

			int sum = num1 + num2 + carry;
			carry = sum / 10;
			curr.next = new ListNode(sum % 10);
			curr = curr.next;

			if (l1 != null) {
				l1 = l1.next;
			}

			if (l2 != null) {
				l2 = l2.next;
			}
		}
		if (carry > 0) {
			curr.next = new ListNode(carry);
		}
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
