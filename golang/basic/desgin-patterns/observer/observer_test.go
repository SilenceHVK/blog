package observer

import "testing"

func TestObserver(t *testing.T) {
	iPhone13 := newGoods()
	iPhone13.Subscribe(&User{
		name: "张三",
	})
	iPhone13.Subscribe(&User{
		name: "王五",
	})

	iPhone13.SetInStock(true)
	iPhone13.NotifyAll()
}
