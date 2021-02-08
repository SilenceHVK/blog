package goroutine

import (
	"context"
	"runtime"
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

/*
	runtime.Gosched() 用于让出 CPU 时间片，让出当前 goroutine 的执行权限
	调度器安排其他等待的任务运行，并在下次某个时候从该位置恢复运行，与 Java 中的 yield 类似
*/
func TestRuntimeGosched(t *testing.T) {
	go func() {
		for i := 0; i < 1000; i++ {
			t.Log("子协程执行-> ", i)
		}
	}()

	for i := 0; i < 1000; i++ {
		runtime.Gosched()
		t.Log("主协程执行-> ", i)
	}
}

/*
	runtime.Goexit() 立即终止当前 goroutine 执行
*/
func TestRuntimeGoexit(t *testing.T) {
	var wg sync.WaitGroup
	wg.Add(1)
	go func() {
		defer t.Log("A defer")
		func() {
			defer wg.Done()
			defer t.Log("B defer")
			runtime.Goexit()
			t.Log("B.....")
		}()
		t.Log("A......")
	}()
	wg.Wait()
}
