package web

import (
	"net/http"
	"testing"
)

func TestCreateServer(t *testing.T) {
	/*
		 	创建服务器监听，第一个参数为网络地址，第二个参数为请求处理器
			网络地址为空字符串 则默认为使用 80 端口
		  请求处理器为nil，则默认采用多路复用
	*/
	//http.ListenAndServe("", nil)

	// 使用 Server 构造器构建，可以进行更为详细的配置
	server := http.Server{Addr: "", Handler: nil}
	server.ListenAndServe()
}

func TestGeneratorSSL(t *testing.T) {
	//max := new(big.Int).Lsh(big.NewInt(1), 128)
	//serialNumber, _ := rand.Int(rand.Reader, max)
}
