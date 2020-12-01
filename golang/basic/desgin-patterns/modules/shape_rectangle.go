package modules

import "fmt"

type Rectangle struct {
}

func (r Rectangle) Draw() {
	fmt.Println("长方形")
}
