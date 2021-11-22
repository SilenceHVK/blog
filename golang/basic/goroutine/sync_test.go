package goroutine

import (
	"sync"
	"testing"
)

// Mutex 互斥锁，实现了 sync.Locker 接口
func TestMutex(t *testing.T) {
	var count = 0
	// 创建互斥锁，Mutex 的零值是 goroutine 未加锁状态，因此不需要额外的初始化
	var mu sync.Mutex

	var wg sync.WaitGroup
	wg.Add(10)

	for i := 0; i < 10; i++ {
		go func() {
			defer wg.Done()
			for j := 0; j < 100000; j++ {
				mu.Lock()   // 加锁
				count++     // 非原子操作，将执行分为三步执行，读取原始值、计算+1、赋值
				mu.Unlock() // 解锁
			}
		}()
	}

	wg.Wait()
	t.Log(count)
}
