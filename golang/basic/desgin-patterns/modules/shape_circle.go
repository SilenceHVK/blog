package modules

import "fmt"

type Circle struct {
}

func (c Circle) Draw() {
	fmt.Println("圆形")
}
