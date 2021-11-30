package main

import (
	"context"
	"fmt"
	"github.com/SilenceHVK/blog/golang/grpc/auth-grpc/proto-source/proto"
	"google.golang.org/grpc"
	"google.golang.org/grpc/metadata"
	"google.golang.org/protobuf/types/known/emptypb"
)

type authCredential struct {
}

func (a authCredential) GetRequestMetadata(ctx context.Context, uri ...string) (map[string]string, error) {
	return map[string]string{
		"appid": "123456",
		"token": "qwerty",
	}, nil
}

// RequireTransportSecurity 在传输时是否加密
func (a authCredential) RequireTransportSecurity() bool {
	return false
}

func main() {
	// 自定义客户端拦截器
	authInterceptor := func(ctx context.Context, method string, req, reply interface{}, cc *grpc.ClientConn, invoker grpc.UnaryInvoker, opts ...grpc.CallOption) error {
		// 创建 metadata 用于传递数据, metadata 是一个 key-value 的结构, key 是 string 类型, value 是 []string 类型 ,作用类似于 HTTP 的 Header
		md := metadata.New(map[string]string{
			"appid": "123456",
			"token": "qwerty",
		})
		ctx = metadata.NewOutgoingContext(ctx, md)
		// 执行 rpc 调用
		return invoker(ctx, method, req, reply, cc)
	}

	interceptors := []grpc.DialOption{
		grpc.WithInsecure(),
		grpc.WithUnaryInterceptor(authInterceptor),
		grpc.WithPerRPCCredentials(authCredential{}), // 使用 WithPerRPCCredentials 实现授权验证，底层逻辑与自定义拦截器是一样的
	}

	conn, _ := grpc.Dial("127.0.0.1:9999", interceptors...)
	defer conn.Close()
	client := proto.NewEmployeeServiceClient(conn)
	employees, err := client.GetAllEmployees(context.Background(), &emptypb.Empty{})
	if err != nil {
		panic(err)
	}
	fmt.Println(employees.Employees)
}
