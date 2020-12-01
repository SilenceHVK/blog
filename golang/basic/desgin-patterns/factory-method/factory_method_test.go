package factory_method

import (
	"github.com/SilenceHVK/blog/golang/basic/desgin-patterns/modules"
	"testing"
)

func TestFactoryMethod(t *testing.T) {
	var shape modules.Shape
	factory := ShapeFactory{}
	shape = factory.Create(CIRCLE)
	shape.Draw()

	shape = factory.Create(RECTANGLE)
	shape.Draw()

	shape = factory.Create(TRIANGLE)
	shape.Draw()
}
