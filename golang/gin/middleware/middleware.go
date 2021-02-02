package middleware

import (
	"log"

	"github.com/gin-gonic/gin"
)

// 自定义中间件
func IPAuthMiddleware() gin.HandlerFunc {
	return func(context *gin.Context) {
		log.Fatalf("客户端IP: %s \n", context.ClientIP())
	}
}
