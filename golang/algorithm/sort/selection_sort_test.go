package sort

import (
	"hvkcoder/algorithm"
	"testing"
)

// 选择排序
func TestSelectionSort(t *testing.T) {
	data := algorithm.GeneratorData(10, 100)
	t.Log("未排序数据 => ", data)
	for i := range data {
		min := i
		for j := i + 1; j < len(data); j++ {
			if data[min] > data[j] {
				min = j
			}
		}
		if i != min {
			data[i], data[min] = data[min], data[i]
		}
	}
	t.Log("已排序数据 => ", data)
}
