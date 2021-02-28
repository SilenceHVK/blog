package io

import (
	"io/ioutil"
	"testing"
)

// 读取指定文件内容，返回一个 []byte
func TestReadFile(t *testing.T) {
	content, err := ioutil.ReadFile("file_test.go")
	if err != nil {
		panic(err)
	}
	t.Log(string(content))
}

// 向文件中写入内容，如果文件不存在则创建，写入前将清空文件中的内容
func TestWriteFile(t *testing.T) {
	err := ioutil.WriteFile("content2.txt", []byte("黄河入海流"), 0777)
	if err != nil {
		panic(err)
	}
	t.Log("写入数据成功")
}

// 显示指定目录下的文件
func TestReadDir(t *testing.T) {
	dirs, err := ioutil.ReadDir("../io")
	if err != nil {
		panic(err)
	}

	for _, dir := range dirs {
		t.Log(dir.Name())
	}
}
