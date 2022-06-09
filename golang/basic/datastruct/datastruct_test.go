package datastruct

import (
	array_list "github.com/SilenceHVK/blog/golang/basic/datastruct/array-list"
	"github.com/SilenceHVK/blog/golang/basic/datastruct/linked_list"
	"github.com/SilenceHVK/blog/golang/basic/datastruct/queue"
	"github.com/SilenceHVK/blog/golang/basic/datastruct/stack"
	"testing"
)

// 线性表
func TestArrayList(t *testing.T) {
	//arrayList := array_list.NewArrayList[string](10)
	arrayList := array_list.DefaultArrayList[string]()
	t.Log(arrayList.String())
	arrayList.Add("Java1")
	arrayList.Add("Java2")
	arrayList.Add("Java3")
	arrayList.AddByIndex("Java4", 2)
	arrayList.AddByIndex("Java5", 2)
	t.Log(arrayList.Size())
	t.Log(arrayList.IsEmpty())
	t.Log(arrayList.String())
	t.Log(arrayList.RemoveByIndex(2))
	t.Log(arrayList.String())

}

// 栈
func TestArrayStack(t *testing.T) {
	stack := stack.NewArrayStack()
	stack.Push("张三1")
	stack.Push("张三2")
	t.Log(stack)
	t.Log(stack.GetSize())
	t.Log(stack.IsEmpty())
	t.Log(stack.Peek())
	t.Log(stack.Pop())
	t.Log(stack)
	t.Log(stack.Peek())
	stack.Push("张三15")
	t.Log(stack)
}

// 队列
func TestQueue(t *testing.T) {
	newQueue := queue.NewQueue[string](10)
	newQueue.Push("Java")
	newQueue.Push("Golang")
	newQueue.Push("Python")
	newQueue.Push("Rust")
	newQueue.Push("Ruby")
	t.Log(newQueue.Pop())
	t.Log(newQueue.Pop())
	t.Log(newQueue.Pop())
	t.Log(newQueue.Pop())
	t.Log(newQueue.Pop())
}

// 链表
func TestLinkedList(t *testing.T) {
	linkedList := linked_list.NewLinkedList[string]()
	linkedList.Add("军长")
	linkedList.Add("师长")
	linkedList.Add("团长")
	linkedList.AddByIndex("旅长", 2)
	linkedList.Add("营长")
	linkedList.Add("连长")
	linkedList.Add("排长")
	linkedList.Add("班长")
	t.Log(linkedList)
	t.Log(linkedList.GetNode(2))

	t.Log(linkedList.RemoveByIndex(2))
	t.Log(linkedList)
}
