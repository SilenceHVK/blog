/*
 工厂方法模式又称为工厂模式，它属于类创建型模式。在工厂方法模式中，工厂父类负责定义创建产品对象的公共接口，而工厂子类负责生成具体的产品对象
*/
package factory_method

import "github.com/SilenceHVK/blog/golang/basic/desgin-patterns/modules"

// 定义形状常量
const (
	CIRCLE    = "CIRCLE"
	RECTANGLE = "RECTANGLE"
	TRIANGLE  = "TRIANGLE"
)

// 定义形状工厂
type ShapeFactory struct {
}

// 根据不同形状类型返回对相应的形状实例
func (factory ShapeFactory) Create(shapeName string) modules.Shape {
	var shape modules.Shape
	switch shapeName {
	case CIRCLE:
		shape = &modules.Circle{}
	case RECTANGLE:
		shape = &modules.Rectangle{}
	case TRIANGLE:
		shape = &modules.Triangle{}
	}
	return shape
}
