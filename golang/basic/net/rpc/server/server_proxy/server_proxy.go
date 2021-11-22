package server_proxy

import (
	"github.com/SilenceHVK/blog/golang/basic/net/rpc/handler"
	"net/rpc"
)

// RegisterGoodsService 将服务注册到 RPC 中
func RegisterGoodsService(service handler.GoodsService) error {
	return rpc.RegisterName(handler.GoodsServiceRPC, service)
}
