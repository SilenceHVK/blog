package inversion_of_control

import "testing"

func TestIoC(t *testing.T) {
	calculator := NewCalculator()

	result := calculator.Add(5).Sub(10).Add(30).Result
	t.Logf("计算结果: %d\n", result)

	if result, err := calculator.Undo(); err == nil {
		t.Logf("计算结果: %d\n", result.Result)
	}
}
