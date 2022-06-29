package utils

import (
	"flag"
	"fmt"
	"os"
	"testing"
)

// flag 包用来处理命令行参数，支持 bool、int、int64、uint、uint64、float float64、string、duration 等数据类型

// 定义 Flag 命令行参数
func TestDefinedFlagArgs(t *testing.T) {
	// flag.Type(flag名, 默认值, 帮助信息)*Type，
	name := flag.String("name", "", "用户名称")
	t.Log(name)

	// flag.TypeVar(Type指针, flag名, 默认值, 帮助信息)
	var age int
	flag.IntVar(&age, "age", 18, "用户年龄")
	t.Log(age)
}

// 解析 Flag 参数
func TestParseArgs(t *testing.T) {
	var name string
	var age int

	// 设置 Flag Usage
	flag.Usage = func() {
		fmt.Printf("Usage: %s -name=value -age=value \n", os.Args[0])
	}
	flag.StringVar(&name, "name", "", "用户名")
	flag.IntVar(&age, "age", 18, "年龄")

	// 定义完 Flag 参数后，通过 Parse() 方法对命令行参数解析
	flag.Parse()

	// 支持的命令行参数格式有以下几种：
	// -flag key
	// -–flag key
	// -flag  key=value
	// --flag key=value

	// 返回命令行参数的其他参数
	flag.Args()
	// 返回命令行参数后的参数个数
	flag.NArg()
	// 返回使用命令行解析的参数个数
	flag.NFlag()
}
