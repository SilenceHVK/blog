package goroutine

/*
	channel 类似于 unix 中的 pipe（管道），是先进先出的，多个 goroutine 同时访问，不需要加锁，channel 是有类型的
	channel 是基于 CSP（Communication Sequential Process）模型设计
*/

import (
	"fmt"
	"sync"
	"testing"
)

func TestChannel(t *testing.T) {
	// channel 在使用前，必须使用 make 进行初始化，channel 只能存储声明的类型
	ch := make(chan int)

	go func() {
		// 输出 channel 值
		t.Logf("Channel value = %d", <-ch)
	}()

	// channel 输入值
	ch <- 1

	// 关闭 channel
	close(ch)
}

// 返回一个发送数据的 channel
func createSendChannel(id int, wg *sync.WaitGroup) chan<- int {
	c := make(chan int)
	go func() {
		for n := range c {
			// 判断 channel 是否关闭
			//n, ok := <-c
			fmt.Printf("Worker %d received %c\n", id, n)
			wg.Done()
		}
	}()
	return c
}

func TestDefineChannel(t *testing.T) {
	var channels [10]chan<- int
	var wg sync.WaitGroup

	for i := 0; i < 10; i++ {
		channels[i] = createSendChannel(i, &wg)
	}

	for i := 0; i < 10; i++ {
		wg.Add(1)
		channels[i] <- 'a' + i
	}
	wg.Wait()
}
