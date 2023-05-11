package map_reduce

import (
	"testing"
)

type Employee struct {
	Name     string
	Age      int
	Vacation int
	Salary   int
}

var list []Employee

func init() {
	list = []Employee{
		{"张三", 40, 0, 8000},
		{"李四", 34, 10, 5000},
		{"王五", 20, 5, 9000},
		{"赵六", 50, 2, 7500},
		{"孙七", 25, 8, 4000},
	}
}

func TestMap(t *testing.T) {
	names := Map(list, func(employee Employee) string {
		return employee.Name
	})
	t.Log(names)

	ages := Map(list, func(employee Employee) int {
		return employee.Age
	})
	t.Log(ages)
}

func TestReduce(t *testing.T) {
	salaryTotal := Reduce(list, func(employee Employee) int {
		return employee.Salary
	})
	t.Logf("%v", salaryTotal)
}

func TestFilter(t *testing.T) {
	employees := Filter(list, func(t Employee) bool {
		return t.Vacation > 0
	})
	t.Log(employees)
}
