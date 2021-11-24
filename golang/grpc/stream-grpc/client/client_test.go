package client

import (
	"context"
	"github.com/SilenceHVK/blog/golang/grpc/stream-grpc/proto-source/proto"
	"google.golang.org/grpc"
	"testing"
	"time"
)

var conn grpc.ClientConn
var client proto.StreamClient

func init() {
	conn, _ := grpc.Dial("127.0.0.1:9999", grpc.WithInsecure())
	client = proto.NewStreamClient(conn)
}

// TestServerStream 服务端流
func TestServerStream(t *testing.T) {
	defer conn.Close()
	stream, _ := client.GetStream(context.Background(), &proto.StreamReqData{
		Data: "当前时间",
	})
	for {
		// 接收服务端消息
		resp, err := stream.Recv()
		if err != nil {
			t.Error(err)
			break
		}
		t.Log(resp)
	}
}

// TestServerStream 客户端流测试
func TestClientStream(t *testing.T) {
	defer conn.Close()
	stream, _ := client.PutStream(context.Background())
	for {
		// 向服务端发送消息
		err := stream.Send(&proto.StreamReqData{
			Data: "当前时间：" + time.Now().String(),
		})
		if err != nil {
			t.Error(err)
			break
		}
		time.Sleep(time.Second)
	}
}

// TestBidirectionalStream 双向数据流
func TestBidirectionalStream(t *testing.T) {
	defer conn.Close()
	stream, _ := client.AllStream(context.Background())
	languages := []string{"java", "golang", "c", "javascript", "python", "lua"}
	for _, value := range languages {
		stream.Send(&proto.StreamReqData{
			Data: value,
		})

		resp, _ := stream.Recv()
		t.Log("服务端数据 => ", resp.Data)
	}
}
