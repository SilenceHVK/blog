package linked_list

import "fmt"

type Node[T any] struct {
	Data T
	Next *Node[T]
}

type LinkedList[T any] struct {
	Head *Node[T]
	Last *Node[T]
	size int
}

// GetNode 获取指定位置的节点
func (l *LinkedList[T]) GetNode(index int) *Node[T] {
	if index > l.size {
		panic("超出链表最大索引")
	}

	node := l.Head
	for i := 0; i < index; i++ {
		node = node.Next
	}
	return node
}

// Add 在链表末尾插入元素值
func (l *LinkedList[T]) Add(e T) {
	l.AddByIndex(e, l.size)
}

// AddByIndex 在指定位置插入值
func (l *LinkedList[T]) AddByIndex(e T, index int) {
	if index > l.size {
		panic("超出链表最大索引")
	}

	newNode := &Node[T]{Data: e}
	if index == 0 {
		newNode.Next = l.Head
		l.Head = newNode
		if l.Last == nil {
			l.Last = newNode
		}
	} else if index == l.size {
		l.Last.Next = newNode
		l.Last = newNode
	} else {
		preNode := l.GetNode(index - 1)
		newNode.Next = preNode.Next
		preNode.Next = newNode
	}
	l.size++
}

// RemoveByIndex 删除指定位置的元素
func (l *LinkedList[T]) RemoveByIndex(index int) (oldNode *Node[T]) {
	preNode := l.GetNode(index - 1)
	oldNode = preNode.Next
	preNode.Next = oldNode.Next
	l.size--
	return
}

func (l *LinkedList[T]) String() string {
	var dataStr string
	node := l.Head
	for node != nil {
		dataStr += fmt.Sprintf("%s ", node.Data)
		node = node.Next
	}
	return fmt.Sprintf("data: %s, size: %d\n", dataStr, l.size)
}

func NewLinkedList[T any]() *LinkedList[T] {
	return &LinkedList[T]{}
}
