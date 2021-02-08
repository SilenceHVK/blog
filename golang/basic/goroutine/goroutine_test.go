package goroutine

import (
	"context"
	"sync"
	"testing"
)

/*
	协程：
		- 协程的内存消耗更小：一个线程可以包含多个协程，线程大约 8MB 的内存申请量，协程大约 2KB 的内存申请量
    - 协程上下文切换更快：线程申请内存，需要通过内核申请，协程不需要
*/

// 协程的使用
func TestGoroutine(t *testing.T) {
	var wg sync.WaitGroup
	for i := 0; i < 10; i++ {
		wg.Add(1)
		go func(index int) {
			t.Log(index)
			wg.Done()
		}(i)
	}
	wg.Wait()
}

// 停止协程
func TestCancelGoroutine(t *testing.T) {
	// 初始化一个 context
	parent := context.Background()
	// 生成一个取消的 context
	ctx, cancel := context.WithCancel(parent)

	var runTime int
	var wg sync.WaitGroup

	wg.Add(1)
	go func(ctx context.Context) {
		for {
			select {
			case <-ctx.Done():
				t.Log("Goroutine Done")
				return
			default:
				t.Logf("Groutine Running Time: %d\n", runTime)
				runTime += 1
			}
			if runTime > 10 {
				// 取消协程
				cancel()
				wg.Done()
			}
		}
	}(ctx)
	wg.Wait()
}
