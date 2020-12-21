package error_handling

/*
	defer 确保调用在函数结束时发生，defer 可以理解为 java 中的 finally
	defer 列表为后进先出
	defer 会推迟资源的释放，因此 defer 尽量不要放到循环语句里
*/

import (
	"bufio"
	"fmt"
	"os"
	"testing"
)

func Fibonacci() func() int {
	a, b := 0, 1
	return func() int {
		a, b = b, a+b
		return a
	}
}

func TestWriterDefer(t *testing.T) {
	file, err := os.Create("fibonacci.txt")
	if err != nil {
		panic(err)
	}
	// 一般 defer 语句放在错误检查语句之后
	defer file.Close()

	writer := bufio.NewWriter(file)
	defer writer.Flush()

	f := Fibonacci()
	for i := 0; i < 20; i++ {
		fmt.Fprintln(writer, f())
	}
}

/*
	defer 函数的实参在注册时通过值拷贝传递，
*/
func TestDeferTheParticipation(t *testing.T) {
	a := 0
	defer func(i int) {
		t.Log("defer i = ", i)
	}(a)
	a++
	t.Log("a = ", a)
}

/*
	defer 必须注册才能执行，如果 defer 位于 return 之后，因为没有注册，所以不会执行
*/
func TestDeferRegister(t *testing.T) {
	defer func() {
		t.Log("Finally.")
	}()
	t.Log("Starting....")
	return

	defer func() {
		t.Log("End.")
	}()
}

/*
	主动调用 os.Exit(int) 退出进程时，defer 将不再被执行，即是 defer 已经提前注册
*/
func TestCallOsExit(t *testing.T) {
	defer func() {
		t.Log("Finally.")
	}()
	t.Log("Starting....")
	os.Exit(0)
}
