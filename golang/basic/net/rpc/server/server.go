package main

import (
	"github.com/SilenceHVK/blog/golang/basic/net/rpc/server/model"
	"github.com/SilenceHVK/blog/golang/basic/net/rpc/server/server_proxy"
	"net"
	"net/rpc"
)

func main() {
	server_proxy.RegisterGoodsService(&model.Goods{})
	listen, _ := net.Listen("tcp", ":9999")
	defer listen.Close()
	conn, _ := listen.Accept()
	rpc.ServeConn(conn)
	defer conn.Close()
}
