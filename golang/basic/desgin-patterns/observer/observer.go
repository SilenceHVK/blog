// 观察者模式 属于行为模式，定义对象间的一对多依赖关系，当订阅对象状态发生改变时，其相关观察对象都会得到通知并更新
package observer

// observer 观察者接口
type observer interface {
	Action()
}

// subscriber 订阅者接口
type subscriber interface {
	Subscribe(observer)
	NotifyAll()
}
