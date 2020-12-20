package errhandling

/*
	defer 确保调用在函数结束时发生
  参数在 defer 语句时计算
	defer 列表为后进先出
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
	defer file.Close()
	if err != nil {
		panic(err)
	}

	writer := bufio.NewWriter(file)
	defer writer.Flush()

	f := Fibonacci()
	for i := 0; i < 20; i++ {
		fmt.Fprintln(writer, f())
	}
}
