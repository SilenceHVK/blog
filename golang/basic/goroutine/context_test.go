package goroutine

import (
	"context"
	"fmt"
	"testing"
	"time"
)

/*
  Context 是 Go 1.7 加入的新的标准库， 它定义了 Context 类型，专门用来处理请求的多个 goroutine 之间与请求域的数据、取消信号、截止时间等相关操作。
  对服务器传入的请求应该创建上下文，而对服务器传出调用应该接手上下文，它们之间的函数调用链必须传递上下文，或者可以使用 WithCancel、WithDeadline、WithValue 创建的派生上下文。当一个上下文取消时，
	它派生的所有上下文也被取消。

	context.Context 是一个接口，定义如下
	type Context interface {
		Deadline() (deadline time.Time, ok bool)  // 返回当前 Context 被取消的时间，即完成工作的截止时间（deadline）
		Done() <-chan struct{}	// 返回一个 Channel ，这个 Channel 会在当前工作完成或者上下文被取消之后关闭，多次调用该方法，返回的同一个 Channel
    Err() error		// 返回当前 Context 结束的原因，它只会在 Done 返回的 Channel 被关闭时才会返回非空值，如果当前 Context 被取消就会返回 Canceled 错误，如果 Context 超时会返回 DeadlineExceeded 错误
    Value(key interface{}) interface{} // 从 Context 中返回key对应的 value，对于同一个上下文来说，多次调用 Value 并传入相同的 Key 会返回相同结果，该方法仅用于传递跨 API 和 进程间跟请求域的数据
  }

	Go内置了两个函数 Background() 和 TODO() ，这两个函数分别实现了 Context 接口的 background 和 todo。
	Background() 主要用于 main 函数、初始化以及测试代码中，作为 Context 这个树结构的最顶层的 Context ，即根 Context
  TODO() 作为不知道该使用什么 Context 的时候，可以使用这个

	background 和 todo 本质上都是 emptyCtx 结构体类型，是一个不可取消，没有截止时间，没有携带任何值的 Context

	context 下还定义了四个 with 系列函数：

 	// 返回带有新 Done 通道的父节点的副本，当调用返回的 cancel 函数或当关闭父上下文的 Done 通道时，将关闭返回上下文的 Done 通道
 	func WithCancel(parent Context) (ctx Context, cancel CancelFunc)
 	// 返回父上下文的副本，并将 deadline 调整为不迟于 d，如果父上下文的 deadline 已经早于 d ，则 WidthDeadline 语义上等同于父上下文。当截止日期过期或调用 cancel 函数时，返回的上下文 Done 通道将别关闭
 	func WithDeadline(parent Context, d time.Time) (Context, CancelFunc)
 	// 与 WithDeadline 相同，其底层调用的就是 WithDeadline(parent, time.Now().Add(timeout))
 	func WithTimeout(parent Context, timeout time.Duration) (Context, CancelFunc)
 	// 将请求作用域的数据与 Context 对象建立关系，仅对 API 和进程间传递请求域的数据使用上下文值，而不是使用它来传递可选值给函数,所提供的键必须是可比较的
  // 并且不应该是 string 类型或任何其他内置类型，以避免使用上下文在包之间发生冲突。
 	func WithValue(parent Context, key, val interface{}) Context，上下文通过具有具体类型 struct{}，或者导出的上下文关键变量的静态类型应该是指针或者接口

	Context 使用注意事项
	1. 不要把 Context 放在结构体中，要以参数的方式显示传递
  2. 以 Context 作为参数的函数方法，应该把 Context 作为第一个参数
  3. 给一个函数传递 Context 的时候，不要传递 nil，如果不知道要传递什么，使用 context.TODO
  4. Context 的 Value 相关方法应该传递请求域的必要数据，不应该用于传递可选参数
  5. Context 是线程安全的，可以放心使用在多个 goroutine 中传递
*/

func gen(ctx context.Context) <-chan int {
	result := make(chan int)
	count := 0
	go func() {
		for {
			select {
			case <-ctx.Done():
				return
			case result <- count:
				count++
			}
		}
	}()
	return result
}

func TestWithCancel(t *testing.T) {
	ctx, cancel := context.WithCancel(context.Background())
	defer cancel()
	for n := range gen(ctx) {
		println(n)
		if n == 5 {
			break
		}
	}
}

func TestWithDeadline(t *testing.T) {
	// 设置 5 秒后自动停止
	d := time.Now().Add(5 * time.Second)
	ctx, cancel := context.WithDeadline(context.Background(), d)
	defer cancel()

	for {
		select {
		case <-ctx.Done():
			fmt.Println("got stop => " + ctx.Err().Error())
			return
		default:
			fmt.Println("still working.....")
			time.Sleep(1 * time.Second)
		}
	}
}
