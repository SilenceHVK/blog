package handler

// GoodsServiceRPC 定义 RPC 服务名称
const GoodsServiceRPC = "handler/GoodsService"

// GoodsService 定义商品服务接口
type GoodsService interface {
	GetGoods(category string, reply *string) error
}
