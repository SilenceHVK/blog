package errhandling

import (
	"errors"
	"os"
	"testing"
)

/*
   panic 停止当前函数执行，一直向上返回，执行每一层的 defer，如果没有遇到 recover，程序退出

	 panic 与 os.Exit 的区别
			os.Exit 退出时不会调用 defer 指定的函数
			os.Exit 退出时不输出当前调用栈信息
*/
func TestPanic(t *testing.T) {
	defer func() {
		t.Log("Finally!")
	}()
	t.Log("Start.")
	panic(errors.New("Something error"))
}

func TestOsExit(t *testing.T) {
	defer func() {
		t.Log("Finally!")
	}()
	t.Log("Start.")
	os.Exit(-1)
}

/*
	recover 仅在 defer 调用中使用，可以获得 panic 的值，如果无法处理可以重新 panic
*/
func TestRecover(t *testing.T) {
	defer func() {
		r := recover()
		if err, ok := r.(error); ok {
			t.Log(err)
		} else {
			panic(r)
		}
	}()
	panic(errors.New("this is an error"))
}
