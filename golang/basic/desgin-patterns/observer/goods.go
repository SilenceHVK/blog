package observer

// Goods 定义商品类并实现订阅者接口
type Goods struct {
	observers []observer
	isInStock bool
}

func newGoods() *Goods {
	return &Goods{
		isInStock: false,
	}
}

func (g *Goods) Subscribe(o observer) {
	g.observers = append(g.observers, o)
}

func (g *Goods) NotifyAll() {
	if g.isInStock {
		for _, o := range g.observers {
			o.Action()
		}
	}
}

func (g *Goods) SetInStock(inStock bool) {
	g.isInStock = inStock
}
