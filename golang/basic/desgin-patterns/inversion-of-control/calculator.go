package inversion_of_control

type Calculator struct {
	Result int
	undo   Undo
}

func NewCalculator() *Calculator {
	return &Calculator{}
}

func (calc *Calculator) Add(num int) *Calculator {
	calc.Result += num
	calc.undo.Add(func() {
		calc.Sub(num)
	})
	return calc
}

func (calc *Calculator) Sub(num int) *Calculator {
	calc.Result -= num
	calc.undo.Add(func() {
		calc.Add(num)
	})
	return calc
}

func (calc *Calculator) Undo() (*Calculator, error) {
	return calc, calc.undo.Undo()
}
