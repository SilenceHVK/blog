package leetcode

import (
	"testing"
)

// https://leetcode.cn/problems/reverse-linked-list/
func TestReverseLinkedList(t *testing.T) {
	node := &ListNode{
		Val: 1,
		Next: &ListNode{
			Val: 2,
			Next: &ListNode{
				Val: 3,
				Next: &ListNode{
					Val: 4,
					Next: &ListNode{
						Val: 5,
						Next: &ListNode{
							Val: 6,
						},
					},
				},
			},
		},
	}
	//node = reverseList(node)
	node = reverseListRecursion(node)
	for node != nil {
		t.Log(node.Val)
		node = node.Next
	}
}

func reverseList(head *ListNode) *ListNode {
	var prev *ListNode
	cur := head
	for cur != nil {
		next := cur.Next
		cur.Next = prev
		prev = cur
		cur = next
	}

	return prev
}

// 递归实现反转链表
func reverseListRecursion(head *ListNode) *ListNode {
	if head.Next == nil {
		return head
	}
	res := reverseListRecursion(head.Next)
	head.Next.Next = head
	head.Next = nil
	return res
}
