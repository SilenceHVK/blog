package oop

import "fmt"

/* TODO: 声明结构体 实现封装 */
type People struct {
	name string
}

func (people *People) walk() {
	fmt.Println(people.name + "在走路")
}
