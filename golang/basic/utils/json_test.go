package utils

import (
	"encoding/json"
	"testing"
)

/*
  Marshal 函数只有转换成功后才有返回值，json 的 key 只能是 string 类型
  channel、complex 和 function 是不能被编码成 json 的
	指针在编码的时候会输出指针指向的内容，空指针会输出 null
*/

// map 转 json
func TestMap2Json(t *testing.T) {
	m := map[string]interface{}{
		"name":     "H_VK",
		"age":      18,
		"sex":      "男",
		"language": []string{"Java", "Golang", "JavaScript"},
	}

	// 将 map 转为 json 格式
	if data, err := json.Marshal(m); err == nil {
		t.Logf("%s\n", data)
	}

	// 带代码缩进的json格式转换 prefix 前缀, indent 缩进格式
	if data, err := json.MarshalIndent(m, "", "	"); err == nil {
		t.Logf("%s\n", data)
	}
}

// 将一个结构体转为 JSON
type Student struct {
	Name     string   `json:"name"` // 通过结构体标签更改 json 键名
	Age      int      `json:"age"`
	Sex      string   `json:"sex"`
	Language []string // 没有设置结构体标签的不会更改
	address  string   // 私有属性不会被转换
	School            // 匿名字段会当成该结构体字段进行处理，相同的键名则不会被转换
}

type School struct {
	Name     string
	Language []string
}

func TestStruct2Json(t *testing.T) {
	student := &Student{
		Name:     "H_VK",
		Age:      18,
		Sex:      "男",
		Language: []string{"Java", "Golang", "JavaScript"},
		address:  "北京",
		School:   School{Name: "北京", Language: []string{"中文", "英文"}},
	}
	if data, err := json.Marshal(student); err == nil {
		t.Logf("%s\n", data)
	}
}

// 将一个 json 转为 结构体
func TestJson2Struct(t *testing.T) {
	jsonStr := "[" +
		"{\"name\":\"H_VK\",\"age\":18,\"sex\":\"男\",\"Language\":[\"Java\",\"Golang\",\"JavaScript\"],\"Name\":\"清华\"}," +
		"{\"name\":\"张三\",\"age\":20,\"sex\":\"女\",\"Language\":[\"Ruby\",\"Python\"],\"Name\":\"北大\"}" +
		"]"

	var students []Student
	err := json.Unmarshal([]byte(jsonStr), &students)
	if err != nil {
		panic(err)
	}
	t.Log(students)
}
