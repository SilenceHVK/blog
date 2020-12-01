/*
  装饰模式，一种动态地往一个类中添加新的行为的设计模式，结构型模式。相比生成子类更为灵活，可以给某个对象而不是整个类添加功能
*/
package decorator

import (
	"fmt"
	"github.com/SilenceHVK/blog/golang/basic/desgin-patterns/modules"
)

// 定义抽象的装饰器
type ShapeDecorator interface {
	modules.Shape
	setBorder()
}

// 具体的装饰器实现
type ReadShapeDecorator struct {
	shape modules.Shape
}

func (r *ReadShapeDecorator) Draw() {
	r.shape.Draw()
	r.setBorder()
}

func (r *ReadShapeDecorator) setBorder() {
	fmt.Println("设置 Border 为红色")
}
