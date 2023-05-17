package inversion_of_control

import "errors"

type Undo []func()

func (undo *Undo) Add(function func()) {
	*undo = append(*undo, function)
}

func (undo *Undo) Undo() error {
	functions := *undo
	if len(functions) == 0 {
		return errors.New("No functions to undo")
	}

	index := len(functions) - 1
	if function := functions[index]; function != nil {
		function()
		functions[index] = nil
	}
	*undo = functions[:index]
	return nil
}
