package algorithm

import "math/rand"

// GeneratorData 生成随机数据
func GeneratorData(size, maxValue int) []int {
	data := make([]int, size)
	for i := range data {
		data[i] = rand.Intn(maxValue)
	}
	return data
}
