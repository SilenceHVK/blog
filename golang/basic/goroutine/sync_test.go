package goroutine

import (
	"fmt"
	"github.com/petermattis/goid"
	"runtime"
	"strings"
	"sync"
	"sync/atomic"
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

// Mutex 不是可重入锁，因为在 Mutex 中没有记录哪个 goroutine 持有该锁
// 可通过记录持有该锁的 goroutine id，由于每个go的版本 stack 输出的格式不一样，则使用 petermattis/goid 获取 goroutine id
func GetGoroutineId() string {
	var buf [64]byte
	// 获取当前 goroutine 的信息，第二个参数如果为 true 则输出所有 goroutine 信息
	n := runtime.Stack(buf[:], false)
	return strings.Fields(strings.TrimPrefix(string(buf[:n]), "goroutine"))[0]
}

// 可重入的 Mutex，并实现 Locker 接口
type ReentrantMutex struct {
	sync.Mutex
	owner          int64 // 当前持有锁的 goroutine id
	reentrantCount int32 // goroutine 重入次数
}

func (m *ReentrantMutex) Lock() {
	gid := goid.Get()
	// 如果当前持有锁的 goroutine id 为本次调用的 goroutine id，则为重入
	if atomic.LoadInt64(&m.owner) == gid {
		m.reentrantCount++
		return
	}
	// 记录持有锁的 goroutine id
	m.Mutex.Lock()
	atomic.StoreInt64(&m.owner, gid)
	m.reentrantCount = 1
}

func (m *ReentrantMutex) Unlock() {
	gid := goid.Get()
	// 如果非持有该锁的 goroutine 尝试释放锁，则抛出异常
	if atomic.LoadInt64(&m.owner) != gid {
		panic(fmt.Sprintf("wrong the owner(%d): %d!", m.owner, gid))
	}
	// 如果该锁未完全是释放，则直接返回
	m.reentrantCount--
	if m.reentrantCount != 0 {
		return
	}
	// 释放该锁
	atomic.StoreInt64(&m.owner, -1)
	m.Mutex.Unlock()
}

// Golang 原生的 Map 是并发不安全的，Sync.Map 保证并发安全
func TestSyncMap(t *testing.T) {
	var users sync.Map

	// 存储数据
	users.Store(10, "H_VK")
	users.Store(20, "张三")
	users.Store(30, "李四")
	users.Store(40, "王五")
	users.Store(50, "赵六")

	// 读取数据
	if value, ok := users.Load(10); ok {
		t.Log(value.(string))
	}

	// 删除数据
	if value, ok := users.LoadAndDelete(30); ok {
		t.Log(value.(string))
	}

	// 遍历数据
	users.Range(func(key, value any) bool {
		t.Log(key, value.(string))
		return true
	})

}

// SyncPool 本质用途是增加临时资源的重用率，减少 GC 负担
// 不适合存储带有状态的对象，因为获取的对象是随机的，并且缓存的对象释放策略完全是由 runtime 内部管理
// 不适合需要控制缓存元素的个数
func TestSyncPool(t *testing.T) {
	// 记录对象创建次数
	var createCount int32
	pool := &sync.Pool{
		New: func() interface{} {
			atomic.AddInt32(&createCount, 1)
			return make([]byte, 1024)
		},
	}

	workerCount := 1024 * 1024
	var wg sync.WaitGroup
	wg.Add(workerCount)

	for i := 0; i < workerCount; i++ {
		go func() {
			defer wg.Done()
			// 申请 buffer 实例
			buffer := pool.Get()
			// 释放 buffer 实例
			defer pool.Put(buffer)
		}()
	}

	wg.Wait()
	t.Logf("%d objects created", createCount)
}
