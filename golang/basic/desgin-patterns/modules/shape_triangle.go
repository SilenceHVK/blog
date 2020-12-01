package modules

import "fmt"

type Triangle struct {
}

func (t Triangle) Draw() {
	fmt.Println("三角形")
}
