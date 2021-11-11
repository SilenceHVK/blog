package observer

import "fmt"

// User 定义用户类并实现观察者接口
type User struct {
	name string
}

func (u *User) Action() {
	fmt.Println(u.name, " 正在付款")
}
