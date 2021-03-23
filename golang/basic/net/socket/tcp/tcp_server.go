package main

import (
	"bufio"
	"fmt"
	"net"
	"strings"
)

func main() {
	// 开启 TCP 服务端监听
	listen, err := net.Listen("tcp", ":8000")
	if err != nil {
		panic(err)
	}
	defer listen.Close()
	fmt.Println("服务端已启动，等待客户端连接.....")

	for {
		// 阻塞等待，等待客户端连接
		client, err := listen.Accept()
		if err != nil {
			panic(err)
		}
		go func() {
			defer client.Close()
			addr := client.RemoteAddr()
			fmt.Println(addr, "连接成功")

			for {
				// 获取客户端发送信息
				message, err := bufio.NewReader(client).ReadString('\n')
				if err != nil {
					fmt.Println(addr, "断开连接")
					break
				}
				// windows 下的以 \r\n 结束，linux 下以 \n 结束
				message = strings.Trim(message, "\r\n")
				fmt.Println(addr, " =>", message)

				// 将获取的消息转为大写返回给客户端
				client.Write([]byte(strings.ToUpper(message)))

				// 如果 message 为 bye，则断开连接
				if message == "bye" {
					fmt.Println(addr, "断开连接")
					break
				}
			}
		}()
	}
}
