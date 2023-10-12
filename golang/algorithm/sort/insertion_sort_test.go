package sort

import (
	"hvkcoder/algorithm"
	"testing"
)

// 插入排序
func TestInsertionSort(t *testing.T) {
	data := algorithm.GeneratorData(10, 100)
	t.Log("未排序数据 => ", data)
	for i := range data {
		for j := i; j > 0 && data[j] < data[j-1]; j-- {
			data[j], data[j-1] = data[j-1], data[j]
		}
	}
	t.Log("已排序数据 => ", data)
}
