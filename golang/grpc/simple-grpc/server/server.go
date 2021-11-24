package main

import (
	"context"
	"github.com/SilenceHVK/blog/golang/grpc/simple-grpc/proto-source/proto"
	"google.golang.org/grpc"
	"net"
)

type Server struct {
	proto.UnsafeGreeterServer
}

func (s Server) SayHello(context context.Context, request *proto.HelloRequest) (*proto.HelloReply, error) {
	return &proto.HelloReply{
		Message: "Hello！" + request.Name,
	}, nil
}

func main() {
	// 创建 GRPC 服务端实例
	server := grpc.NewServer()
	// 将服务注册到 GRPC 中
	proto.RegisterGreeterServer(server, &Server{})
	// 创建服务器监听
	listen, _ := net.Listen("tcp", ":9999")
	defer listen.Close()
	// 将服务处理交由 GRPC 处理
	server.Serve(listen)
}
