package stack

// 栈是一个 FILO（先进后出）的数据结构
type Stack interface {
	// 判断栈是否为空
	IsEmpty() bool
	// 获取栈大小
	GetSize() int
	// 元素入栈
	Push(e interface{})
	// 元素出栈
	Pop() interface{}
	// 查看栈顶元素
	Peek() interface{}
}

// 构建 ArrayStack 结构，并实现 Stack 接口
type ArrayStack struct {
	size     int
	capacity int
	data     []interface{}
}

func (a *ArrayStack) IsEmpty() bool {
	return a.size == 0
}

func (a *ArrayStack) GetSize() int {
	return a.size
}

func (a *ArrayStack) Push(e interface{}) {
	if a.size+1 >= a.capacity {
		a.capacity = a.capacity + (a.capacity >> 1)
		tmp := a.data
		a.data = make([]interface{}, a.capacity)
		copy(a.data, tmp)
	}
	a.data[a.size] = e
	a.size++
}

func (a *ArrayStack) Pop() interface{} {
	if a.size <= 0 {
		panic("index out of range")
	}
	a.size--
	data := a.data[a.size]
	a.data[a.size] = nil
	return data
}

func (a *ArrayStack) Peek() interface{} {
	if a.size <= 0 {
		panic("empty stack")
	}
	return a.data[a.size-1]
}

func NewArrayStack() *ArrayStack {
	return &ArrayStack{data: make([]interface{}, 10), size: 0, capacity: 10}
}
