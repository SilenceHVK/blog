package map_reduce

func Map[T any, R any](data []T, fn func(t T) R) []R {
	var newArray []R
	for _, v := range data {
		newArray = append(newArray, fn(v))
	}
	return newArray
}

func Reduce[T any](data []T, fn func(t T) int) int {
	var sum int
	for _, v := range data {
		sum += fn(v)
	}
	return sum
}

func Filter[T any](data []T, fn func(t T) bool) []T {
	var newArray []T
	for _, v := range data {
		if fn(v) {
			newArray = append(newArray, v)
		}
	}
	return newArray
}
