package decorator

import (
	"github.com/SilenceHVK/blog/golang/basic/desgin-patterns/modules"
	"testing"
)

func TestDecorator(t *testing.T) {
	circle := &modules.Circle{}
	shapeDecorator := &ReadShapeDecorator{}
	shapeDecorator.shape = circle
	shapeDecorator.Draw()
}
