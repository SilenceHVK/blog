package main

import (
	"context"
	"fmt"
	"github.com/SilenceHVK/blog/golang/grpc/simple-practice/proto_source/proto"
	"google.golang.org/grpc"
)

func main() {
	// 创建与服务端连接
	conn, _ := grpc.Dial("127.0.0.1:9999", grpc.WithInsecure())
	// 实例客户端
	client := proto.NewGreeterClient(conn)
	// 调用客户端方法
	reply, _ := client.SayHello(context.Background(), &proto.HelloRequest{
		Name: "H_VK",
	})
	fmt.Println(reply)
}
