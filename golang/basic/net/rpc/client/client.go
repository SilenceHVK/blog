package main

import (
	"fmt"
	"github.com/SilenceHVK/blog/golang/basic/net/rpc/client/client_proxy"
)

func main() {
	service := client_proxy.NewGoodsService("tcp", ":9999")
	var reply string
	service.GetGoods("零食", &reply)
	fmt.Println("客户端返回 => " + reply)
}
