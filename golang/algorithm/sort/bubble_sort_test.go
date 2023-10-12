package sort

import (
	"hvkcoder/algorithm"
	"testing"
)

// 冒泡排序
func TestBubbleSort(t *testing.T) {
	data := algorithm.GeneratorData(10, 100)
	t.Log("未排序数据 => ", data)
	for i := range data {
		for j := 0; j < len(data)-1-i; j++ {
			if data[j] > data[j+1] {
				data[j], data[j+1] = data[j+1], data[j]
			}
		}
	}
	t.Log("已排序数据 => ", data)
}
