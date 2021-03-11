package goroutine

/*
	channel 类似于 unix 中的 pipe（管道），是先进先出的，多个 goroutine 同时访问，不需要加锁，channel 是有类型的
	channel 是基于 CSP（Communication Sequential Process）模型设计
*/

import (
	"fmt"
	"math/rand"
	"sync"
	"testing"
	"time"
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

// 使用 channel 实现生产者/消费者
func TestProducerAndConsumer(t *testing.T) {
	ch := make(chan int)
	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer close(ch)
		for i := 0; i < 10; i++ {
			num := rand.Int()
			t.Log("生产者生产数据 -> ", num)
			ch <- num
			time.Sleep(time.Duration(500) * time.Microsecond)
		}
	}()

	go func() {
		defer wg.Done()
		for data := range ch {
			t.Log("消费者消费数据 -> ", data)
		}
	}()
	wg.Wait()
	t.Log("程序结束......")
}

/*
	计时器类型表示单个事件，当计时器过期时，当前时间将被发送到 Timer 结构体属性的 c 上，除非计时器是使用 After 创建的
	type Timer struct {
		C <-chan Time
		r runtimeTimer
	}
*/
func TestTimerChannel(t *testing.T) {
	// 创建一个新的计时器，它会在至少持续时间 d 之后将当前时间发送到其 channel 上
	newTimer := time.NewTimer(5 * time.Second)
	t.Log("当前时间 = ", time.Now())
	go func() {
		date := <-newTimer.C
		t.Log("newTimer 1s 后的时间 = ", date)
	}()

	// 停止定时器
	newTimer.Stop()
	// 重置计时器时间
	newTimer.Reset(1 * time.Second)

	// After 函数相当于 time.NewTimer(d).C
	after := time.After(5 * time.Second)
	t.Log("当前时间 = ", time.Now())
	afterDate := <-after
	t.Log("after 5s 后的时间 = ", afterDate)
}

/*
Ticker 是一个定时触发的时间计时器，它会以一个间隔（interval）往 channel 发送一个时间（当前时间）
而 channel 的接收者可以以固定的时间间隔从 channel 中读取事件
*/
func TestTickerChannel(t *testing.T) {
	ticker := time.NewTicker(5 * time.Second)
	num := 0
	for {
		num += 5
		t.Log(<-ticker.C)

		if num > 15 {
			ticker.Stop()
			t.Log("定时器结束")
			break
		}
	}
}

/*
 select 可以监听 channel 上的数据流动，语法与 switch 语法类似，如果没有 default，select 将被阻塞
 select 有比较多的限制，其中最大的一条限制就是每个 case 语句里必须是一个 IO 操作
*/
func TestSelect(t *testing.T) {
	var wg sync.WaitGroup
	wg.Add(1)
	ch := make(chan int)
	defer close(ch)

	go func() {
		defer wg.Done()
		for i := 0; i < 8; i++ {
			num := <-ch
			t.Log(num)
		}
	}()

	go func() {
		a, b := 1, 1
		for {
			select {
			case ch <- a:
				a, b = b, a+b
			}
		}
	}()
	wg.Wait()
	t.Log("程序结束")
}
