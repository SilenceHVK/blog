package main

import (
	"bufio"
	"fmt"
	"net"
	"os"
	"strings"
	"sync"
)

func main() {
	var wg sync.WaitGroup
	wg.Add(1)

	// 连接服务器端
	conn, err := net.Dial("tcp", ":8000")
	if err != nil {
		panic(err)
	}

	buf := make([]byte, 1024)
	go func() {
		defer func() {
			conn.Close()
			wg.Done()
		}()
		for {
			// 向服务端发送消息
			fmt.Print("请输入发送消息: ")
			msg, _ := bufio.NewReader(os.Stdin).ReadString('\n')
			_, _ = conn.Write([]byte(msg))

			// 获取服务端消息
			n, err := conn.Read(buf)
			if err != nil {
				fmt.Println("与服务端断开连接")
				break
			}
			fmt.Println("服务端 => ", string(buf[:n]))

			// 如果发送的消息为 bye，则与服务器断开连接
			if strings.Trim(msg, "\r\n") == "bye" {
				fmt.Println("与服务端断开连接")
				break
			}
		}
	}()
	wg.Wait()
}
