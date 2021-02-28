package io

import (
	"io"
	"os"
	"path/filepath"
	"testing"
)

const FILE_PATH = "./file_test.go"

// FileInfo 接口的使用
func TestFileInfo(t *testing.T) {
	fileInfo, err := os.Stat(FILE_PATH)
	if err != nil {
		t.Error(err)
	} else {
		t.Logf("数据类型是: %T\n", fileInfo)
		t.Log("文件名: ", fileInfo.Name())
		t.Log("是否为目录: ", fileInfo.IsDir())
		t.Log("文件大小: ", fileInfo.Size())
		t.Log("文件权限: ", fileInfo.Mode())
		t.Log("文件修改时间: ", fileInfo.ModTime())
	}
}

// 文件路径
func TestFilePath(t *testing.T) {
	t.Log("判断是否是绝对路径：", filepath.IsAbs(FILE_PATH))
	abs, _ := filepath.Abs(FILE_PATH)
	t.Log("获取绝对路径：", abs)
	rel, _ := filepath.Rel("/golang", abs)
	t.Log("获取相对路径：", rel)
	t.Log("获取文件后缀名：", filepath.Ext(FILE_PATH))
	t.Log("获取文件父级目录：", filepath.Dir(FILE_PATH))
	t.Log("拼接路径：", filepath.Join("User", FILE_PATH))
}

// 文件基础操作
func TestFileOperation(t *testing.T) {
	// 创建单个文件夹
	err := os.Mkdir("test", os.ModePerm)
	if err != nil {
		t.Error(err)
	} else {
		t.Log("创建文件夹成功")
	}

	// 创建多级文件夹
	err = os.MkdirAll("test/多级目录/multi", os.ModePerm)
	if err != nil {
		t.Error(err)
	} else {
		t.Log("创建多级目录成功")
	}

	// 创建文件，Create 函数本质上调用的是 os.OpenFile 函数
	file, err := os.Create("test/content.txt")
	defer file.Close()
	if err != nil {
		t.Error(err)
	} else {
		t.Logf("创建文件成功 %v\n", file)
	}

	// 删除文件
	os.Remove("test/content.txt")

	// 删除文件夹
	os.RemoveAll("test")
}

// 文件内容操作
func TestFileContent(t *testing.T) {
	// 打开文件 os.Open 实际上调的就是 os.OpenFile
	// 以读写的方式打开，如果文件不存在则创建
	file, err := os.OpenFile("test", os.O_CREATE|os.O_RDWR, 0777)
	defer file.Close()
	if err != nil {
		t.Error(err)
	}

	// 通过字节数组写入文件内容 WriteString 方法调的就是 字节数组写入
	n, err := file.Write([]byte("Hello byte 中文"))
	if err != nil {
		t.Error(err)
	} else {
		t.Log("字节数组写入成功，写入数 ", n)
	}

	// 读取文件内容
	buf := make([]byte, 1024*8)
	n = -1
	for {
		n, err = file.Read(buf)
		if n == 0 || err == io.EOF {
			t.Log("文件读取结束")
			break
		}
		t.Log(string(buf[:n]))
	}
}
