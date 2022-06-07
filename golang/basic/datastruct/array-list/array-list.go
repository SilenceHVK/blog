package array_list

import "fmt"

const _DEFAULT_CAPACITY = 10

type ArrayList[T any] struct {
	data []T
	size int
}

func (a *ArrayList[T]) Size() int {
	return a.size
}

func (a *ArrayList[T]) IsEmpty() bool {
	return a.size > 0
}

func (a *ArrayList[T]) Add(e T) {
	a.AddByIndex(e, a.size)
}

func (a *ArrayList[T]) AddByIndex(e T, index int) {
	capacity := len(a.data)
	// 判断数组是否需要扩容
	if a.size+1 >= capacity {
		a.grow()
	}
	// 判断索引是否超出数组索引
	if index < 0 || index > a.size {
		panic(fmt.Sprintf("超出数组索引，index: %d, size: %d", index, a.size))
	}
	for i := a.size; i >= index; i-- {
		a.data[i+1] = a.data[i]
	}
	a.data[index] = e
	a.size += 1
}

func (a *ArrayList[T]) RemoveByIndex(index int) (oldValue T) {
	// 判断索引是否超出数组索引
	if index < 0 || index >= a.size {
		panic(fmt.Sprintf("超出数组索引，index: %d, size: %d", index, a.size))
	}
	oldValue = a.data[index]
	for i := index; i < a.size; i++ {
		a.data[i] = a.data[i+1]
	}
	a.size -= 1
	return
}

// String 数组转字符串
func (a *ArrayList[T]) String() string {
	var dataStr string
	if a.size > 0 {
		for i := 0; i < a.size; i++ {
			dataStr += fmt.Sprintf("%v", a.data[i])
			if i != a.size-1 {
				dataStr += " "
			}
		}
	}
	return fmt.Sprintf("data: [%s], size: %d, capacity: %d", dataStr, a.size, len(a.data))
}

// grow 数组扩容方法
func (a *ArrayList[T]) grow() {
	oldCapacity := len(a.data)
	if oldCapacity > 0 {
		newData := make([]T, oldCapacity+(oldCapacity>>1))
		for i := 0; i < a.size; i++ {
			newData[i] = a.data[i]
		}
		//for index, value := range a.data {
		//
		//}
		a.data = newData
	} else {
		a.data = make([]T, _DEFAULT_CAPACITY)
	}
}

// DefaultArrayList 默认初始化 ArrayList
func DefaultArrayList[T any]() *ArrayList[T] {
	return &ArrayList[T]{
		data: make([]T, 0),
	}
}

// NewArrayList 初始化 ArrayList
func NewArrayList[T any](capacity int) *ArrayList[T] {
	return &ArrayList[T]{
		data: make([]T, capacity),
	}
}
