package main

import (
	"fmt"
	"github.com/SilenceHVK/blog/golang/grpc/stream-grpc/proto-source/proto"
	"google.golang.org/grpc"
	"net"
	"strings"
	"time"
)

type Server struct {
	proto.UnsafeStreamServer
}

// GetStream 服务端流模式实现
func (s Server) GetStream(data *proto.StreamReqData, server proto.Stream_GetStreamServer) error {
	for {
		// 向客户端发送消息
		err := server.Send(&proto.StreamRespData{
			Data: data.Data + ":" + time.Now().String(),
		})

		if err != nil {
			return err
		}
		time.Sleep(time.Second)
	}
}

// PutStream 客户端流模式实现
func (s Server) PutStream(server proto.Stream_PutStreamServer) error {
	for {
		// 接收客户端消息
		if resp, err := server.Recv(); err == nil {
			fmt.Println(resp)
		} else {
			fmt.Println(err)
			return err
		}
	}
}

// AllStream 双向流模式实现
func (s Server) AllStream(server proto.Stream_AllStreamServer) error {
	for {
		if resp, err := server.Recv(); err == nil {
			fmt.Println("客户端数据 => ", resp.Data)
			server.Send(&proto.StreamRespData{
				Data: strings.ToUpper(resp.Data),
			})
		} else {
			fmt.Println(err)
			return err
		}
	}
}

func main() {
	server := grpc.NewServer()
	proto.RegisterStreamServer(server, &Server{})
	listen, _ := net.Listen("tcp", ":9999")
	defer listen.Close()
	server.Serve(listen)
}
