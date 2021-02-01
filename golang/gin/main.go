package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
)

func main() {
	// Default returns an Engine instance with the Logger and Recovery middleware already attached.
	// 创建 gin 默认的 Engine 实例
	r := gin.Default()

	// 设置 GET 请求路由
	r.GET("/ping", pong)

	// 服务端监听端口
	r.Run(":8080")
}

func pong(context *gin.Context) {
	context.JSON(http.StatusOK, map[string]interface{}{
		"message": "pong",
	})
}
