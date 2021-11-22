package rpc

import (
	"io"
	"net"
	"net/http"
	"net/rpc"
	"net/rpc/jsonrpc"
	"testing"
)

/*
	RPC（Remote Procedure Call）远程过程调用，简单的理解是一个节点请求另一个节点提供服务
	RPC 中的问题：
		1. Call 的 id 映射
		2. 序列化和反序列化
		3. 网络传输：
			http 协议底层使用的也是 TCP ，http 目前主流的是1.X ,但是是一次性的、一旦返回结果就断开
			基于TCP/UDP协议自己封装一层协议，但是没有通用性，GRPC 采用 http2.0 既有http的特性也有长连接特性
*/

type UserService struct {
}

func (user *UserService) SayHi(name string, reply *string) error {
	*reply = "Hi！" + name
	return nil
}

// RPC 服务端
func TestRPCServer(t *testing.T) {
	// 创建连接监听
	listener, _ := net.Listen("tcp", ":9999")
	defer listener.Close()
	// 将服务注册到 RPC 包下
	rpc.RegisterName("UserService", &UserService{})
	// 阻塞等待客户端连接
	conn, _ := listener.Accept()
	// 将客户端连接交由 RPC 包处理
	//rpc.ServeConn(conn)

	// 将 RPC 底层序列化协议换成 json ，使其支持跨语言调用
	rpc.ServeCodec(jsonrpc.NewServerCodec(conn))
}

// RPC 客户端
func TestRPCClient(t *testing.T) {
	// 与服务端建立连接
	client, _ := rpc.Dial("tcp", ":9999")
	defer client.Close()
	// 调用服务端服务
	var reply string
	client.Call("UserService.SayHi", "hvkcoder", &reply)
	t.Log("服务端返回：" + reply)
}

// RPC 底层序列化设置为 JSON
func TestRPCClientByJSON(t *testing.T) {
	// 与服务端建立连接
	conn, _ := net.Dial("tcp", ":9999")
	defer conn.Close()

	// 设置 RPC 底层序列化协议为 json
	client := rpc.NewClientWithCodec(jsonrpc.NewClientCodec(conn))
	defer client.Close()

	// 调用服务端服务
	var reply string
	client.Call("UserService.SayHi", "hvkcoder", &reply)
	t.Log("服务端返回：" + reply)
}

// RPC 使用 http 协议通信，参数传递为 { id: 0, "method": "UserService.SayHi", "params": [""]}
func TestRPCHttpServer(t *testing.T) {
	rpc.RegisterName("UserService", &UserService{})
	http.HandleFunc("/jsonrpc", func(writer http.ResponseWriter, request *http.Request) {
		var conn io.ReadWriteCloser = struct {
			io.Writer
			io.ReadCloser
		}{
			writer,
			request.Body,
		}
		rpc.ServeCodec(jsonrpc.NewServerCodec(conn))
	})
	http.ListenAndServe(":9999", nil)
}
