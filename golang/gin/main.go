package main

import (
	"github.com/SilenceHVK/blog/golang/gin/handle"
	"github.com/gin-gonic/gin"
)

func main() {

	// 设置 gin 日志输出到文件中
	//file, _ := os.Create("./gin.log")
	//gin.DefaultWriter = io.MultiWriter(file)      // 设置 gin 日志写入
	//gin.DefaultErrorWriter = io.MultiWriter(file) // 设置 gin 错误日志写入

	// Default returns an Engine instance with the Logger and Recovery middleware already attached.
	// 创建 gin 默认的 Engine 实例
	r := gin.Default()
	// 使用 gin Logger, Recovery 中间件
	r.Use(gin.Logger(), gin.Recovery())

	// 使用自定会中间件
	//r.Use(middleware.IPAuthMiddleware())

	// 将自定义验证注册到 Validator 上
	//if validator, ok := binding.Validator.Engine().(*validator.Validate); ok {
	//	validator.RegisterValidation("customValidate", validate.CustomValidate)
	//}

	// 设置静态文件夹绑定
	r.Static("/static", "./static")
	//r.StaticFS("/static", http.Dir("static"))
	//r.StaticFile("/index.html", "/static/index.html") // 设置单个文件

	// 加载模板文件路径
	r.LoadHTMLGlob("template/*")
	r.GET("/index", handle.RenderTemplate)

	// 设置 GET 请求路由
	r.GET("/ping", handle.Get)

	// 设置 POST 请求路由
	r.POST("/post", handle.Post)

	// 设置自定义 httpMethod 请求路由
	r.Handle("DELETE", "/delete", handle.Delete)

	// 设置 支持所有请求方法路由
	r.Any("/any", handle.Any)

	// 设置路由参数
	r.GET("/get/:name", handle.GetName)

	// 获取 url 参数 ?key=value
	r.GET("/query", handle.GetQuery)

	// 获取 form 表单提交数据
	r.POST("/form", handle.PostBody)

	// 根据提交的数据，绑定结构体
	r.Any("/bind", handle.BindStruct)

	// 服务端监听端口
	r.Run(":8080")
}
