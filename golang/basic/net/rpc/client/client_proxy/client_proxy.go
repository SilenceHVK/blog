package client_proxy

import (
	"github.com/SilenceHVK/blog/golang/basic/net/rpc/handler"
	"net/rpc"
)

// GoodsService 实现 GoodsService接口
type GoodsService struct {
	*rpc.Client
}

func (g *GoodsService) GetGoods(category string, reply *string) error {
	return g.Call(handler.GoodsServiceRPC+".GetGoods", category, &reply)
}

func NewGoodsService(protocol, address string) *GoodsService {
	conn, _ := rpc.Dial(protocol, address)
	return &GoodsService{
		conn,
	}
}
