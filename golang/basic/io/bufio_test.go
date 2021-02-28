package io

import (
	"bufio"
	"io"
	"os"
	"strings"
	"testing"
)

/*
	bufio 包对 io 包下的对象 Reader、Writer 进行包装，提供了数据缓冲功能，能够一定程度减少大块数据读写带来的开销
	缓冲区的设计是为了存储多次的写入，最后一口气把缓冲区内容写入文件
*/

/*
	bufio.Reader(p []byte) 相当于读取大小为 len(p) 的内容
  1. 缓冲区有内容时，将缓冲区内容全部填入 p 并清空缓冲区
  2. 当缓冲区没有内容，且要读取的内容比缓冲区还要大，则直接去文件读取
  3. 当缓冲区没有内容，且要读取的内容小于缓冲区大小，则从文件读取内容充满缓冲区，并将 p 填满
*/
func TestReader(t *testing.T) {
	file, err := os.Open("file_test.go")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	// 新建一个 Reader 实例，缓冲区大小默认为 4096，NewReader 实际上调用的是 NewReaderSize 方法
	reader := bufio.NewReader(file)

	for {
		// 按 delim 分割读取数据，如果在找到 delim 之前遇到错误，则返回遇到错误之前的所有数据
		// ReadString 与 ReadBytes 功能相同，只不过返回的是一个字符串
		buf, err := reader.ReadBytes('\n')
		if err != nil {
			// 文件读取完成
			if err == io.EOF {
				t.Log("文件读取完毕")
				break
			}
			panic(err)
		}
		t.Log(string(buf))
	}
}

/*
	bufio.Write(p []byte)
  1. 判断缓冲区中可用容量是否可以放下 写入内容
  2. 如果能放下，直接将 写入内容 放到缓冲区
  3. 如果放不下，并且此时缓冲区是空的，则直接写入文件
  4. 如果缓冲区的可用容量不能放下 写入内容，并且此时缓冲区非空，则填满缓冲区，把缓冲区内容写入文件中，并清空缓冲区
*/
func TestWriter(t *testing.T) {
	file1, err := os.Open("file_test.go")
	defer file1.Close()
	if err != nil {
		panic(err)
	}

	file2, err := os.OpenFile("file_test.txt", os.O_CREATE|os.O_RDWR, 0777)
	defer file2.Close()
	if err != nil {
		panic(err)
	}

	// 创建 Reader
	reader := bufio.NewReader(file1)

	// 新建一个 Writer 实例，缓冲区大小默认为 4096，NewWriter 实际上调用的是 NewWriterSize 方法
	writer := bufio.NewWriter(file2)

	for {
		buf, err := reader.ReadBytes('\n')
		if err != nil {
			if err == io.EOF {
				t.Log("文件读取完成")
				break
			}
			panic(err)
		}
		// 将内容写入缓冲区，并清空缓冲区
		writer.Write(buf)
		writer.Flush()
	}
}

/*
	Scanner 更适合对数据进行行读取
  Scanner 可以通过 splitFunc 将输入数据拆分为多个 token，然后依次进行读取，而非直接使用 Reader 类
*/
func TestScanner(t *testing.T) {
	// 创建 Reader 对象
	reader := bufio.NewReader(strings.NewReader("Java C# JavaScript Golang Python Ruby Lua"))
	// 创建 Scanner 实例
	scanner := bufio.NewScanner(reader)

	/*
			指定分割方法，bufio 中提供了 四种默认的分割方法
			- ScanBytes：按 byte 进行拆分
		  - ScanLines：按行进行拆分
		  - ScanRunes：按 UTF-8 字符进行拆分
		  - ScanWords：按单词进行拆分
	*/
	scanner.Split(bufio.ScanWords)

	// scanner.Scan() 获取当前位置的 token，该 token 可以通过 Bytes 或 Text 获取，并让 Scanner 的扫描位置移动到下一个 token
	for scanner.Scan() {
		t.Log(scanner.Text())
	}
}
