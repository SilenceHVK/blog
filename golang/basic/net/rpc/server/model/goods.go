package model

type Goods struct {
}

func (g Goods) GetGoods(category string, reply *string) error {
	*reply = category + "：饮料、花生、瓜子、大辣片。。。。。"
	return nil
}
