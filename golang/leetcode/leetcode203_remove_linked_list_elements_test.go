package leetcode

import (
	"testing"
)

// https://leetcode.cn/problems/remove-linked-list-elements/
func TestRemoveLinkedListElement(t *testing.T) {
	node := &ListNode{
		Val: 1,
		Next: &ListNode{
			Val: 2,
			Next: &ListNode{
				Val: 6,
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

	node = removeElementsRecursion(node, 6)

	for node != nil {
		t.Log(node.Val)
		node = node.Next
	}
}

type ListNode struct {
	Val  int
	Next *ListNode
}

// 虚拟头节点实现删除链表指定元素
func removeElements(head *ListNode, val int) *ListNode {
	dummyHead := &ListNode{Next: head}

	prev := dummyHead
	for prev.Next != nil {
		if prev.Next.Val == val {
			prev.Next = prev.Next.Next
		} else {
			prev = prev.Next
		}
	}

	return dummyHead.Next
}

// 递归方式实现删除链表指定元素
func removeElementsRecursion(head *ListNode, val int) *ListNode {
	if head == nil {
		return head
	}

	head.Next = removeElementsRecursion(head.Next, val)

	if head.Val == val {
		return head.Next
	} else {
		return head
	}
}
