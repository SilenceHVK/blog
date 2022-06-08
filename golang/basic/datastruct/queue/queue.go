package queue

type Queue[T any] struct {
	data  []T
	front int // 队列顶部位置
	rear  int // 队列底部位置
}

// Push 入队列方法
func (q *Queue[T]) Push(e T) {
	if (q.rear+1)%len(q.data) == q.front {
		panic("该队列已满，无法压入数据")
	}
	// 将输入压入队列
	q.data[q.rear] = e
	// 更改队列底部位置
	q.rear = (q.rear + 1) % len(q.data)
}

// Pop 出队列方法
func (q *Queue[T]) Pop() (v T) {
	if q.front == q.rear {
		panic("该队列是一个空队列")
	}
	v = q.data[q.front]
	// 更改队列顶部位置
	q.front = (q.front + 1) % len(q.data)
	return v
}

func NewQueue[T any](capacity int) *Queue[T] {
	return &Queue[T]{
		data: make([]T, capacity),
	}
}
