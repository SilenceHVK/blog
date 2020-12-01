package oop

import "fmt"

type Chinese struct {
	// TODO: 通过嵌入的方式，实现继承
	people *People
	skin   string
}

func (c *Chinese) getSkin() string {
	return c.skin
}

/* TODO: 通过定义接口中存在的方法，隐式的实现接口 */
func (c *Chinese) Speak() {
	fmt.Println("中国人：" + c.people.name + " 说中国话")
}
