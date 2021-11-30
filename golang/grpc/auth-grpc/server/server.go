package main

import (
	"context"
	"github.com/SilenceHVK/blog/golang/grpc/auth-grpc/proto-source/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/metadata"
	"google.golang.org/grpc/status"
	"google.golang.org/protobuf/types/known/emptypb"
	"net"
)

type Employee struct {
	proto.UnsafeEmployeeServiceServer
}

func (e Employee) GetAllEmployees(ctx context.Context, empty *emptypb.Empty) (*proto.QueryResp, error) {
	return &proto.QueryResp{
		Employees: []string{
			"张三",
			"李四",
			"王五",
			"赵六",
			"孙七",
		},
	}, nil
}

func main() {
	// 自定义服务端拦截器
	authInterceptor := func(ctx context.Context, req interface{}, info *grpc.UnaryServerInfo, handler grpc.UnaryHandler) (resp interface{}, err error) {
		// 从 metadata 中获取授权信息
		md, ok := metadata.FromIncomingContext(ctx)
		if !ok {
			return resp, status.Error(codes.Unauthenticated, "未接收到 Token")
		}
		var appid, token = "", ""

		if data, ok := md["appid"]; ok {
			appid = data[0]
		}

		if data, ok := md["token"]; ok {
			token = data[0]
		}

		if appid != "123456" || token != "qwerty" {
			return resp, status.Error(codes.Unauthenticated, "Token 认证失败")
		}

		// 执行 rpc 调用
		return handler(ctx, req)
	}

	server := grpc.NewServer(grpc.UnaryInterceptor(authInterceptor))
	proto.RegisterEmployeeServiceServer(server, &Employee{})
	listen, _ := net.Listen("tcp", ":9999")
	defer listen.Close()
	server.Serve(listen)
}
