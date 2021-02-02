package validate

import (
	"reflect"
	"time"

	"github.com/go-playground/validator/v10"
)

// 自定义验证规则
func CustomValidate(v *validator.Validate, topStruct reflect.Value, currentStructOrField reflect.Value, field reflect.Value, fieldType reflect.Type, fieldKind reflect.Kind, param string) bool {
	if date, ok := field.Interface().(time.Time); ok {
		now := time.Now()
		return date.Unix() > now.Unix()
	}
	return true
}
