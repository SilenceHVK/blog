package stack

import "testing"

func TestArrayStack(t *testing.T) {
	stack := NewArrayStack()
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
