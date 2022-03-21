package generic

import (
	"fmt"
	"strconv"
	"testing"
)

// golang 1.18 新特性 -> 泛型

// 定义泛型函数
func printSlice[T any](s []T) {
	for _, v := range s {
		fmt.Print(v, " ")
	}
	fmt.Println()
}

// 定义泛型切片
type vector[T any] []T

// 定义泛型map
type M[K string, V any] map[K]V

// 定义泛型通道
type C[T any] chan T

// 通过 interface 约束泛型类型
type Num interface {
	// 不带波浪符号为严格类型，即不能满足自定义类型
	~int | ~float64
}
type NumStr interface {
	string | Num
}

func add[T NumStr](a, b T) T {
	return a + b
}

// 通过 interface 中的定义的方法约束泛型参数，
// 与直接使用接口的区别在于，接口必须实现全部接口方法，才能满足 ; 泛型只需实现特定的方法
type ShowPrice interface {
	ToString() string
}

type Price int

func (p Price) ToString() string {
	return strconv.Itoa(int(p))
}

func ShowPriceList[T ShowPrice](priceList []T) (res []string) {
	for _, v := range priceList {
		res = append(res, v.ToString())
	}
	return
}

func TestGeneric(t *testing.T) {
	printSlice([]int{1, 3, 5, 7, 9})
	printSlice([]string{"Golang", "Java", "Python"})

	t.Log(vector[int]{2, 4, 6, 8, 10})
	t.Log(vector[string]{"Angular", "React", "Vue"})

	t.Log(M[string, string]{
		"周杰伦": "退后",
		"许嵩":  "玫瑰花的葬礼",
	})

	ch := make(C[string], 10)
	ch <- "Hello"
	ch <- "World"
	t.Log(<-ch, <-ch)

	t.Log(add(1, 2))
	t.Log(add("Hello", " Golang"))

	t.Log(ShowPriceList([]Price{30, 40, 50}))
}
