package web_framework

import (
	"net/http"
)

type Engine struct {
	router *router
}

// New 实例化 Web 引擎
func New() *Engine {
	return &Engine{
		router: newRouter(),
	}
}

// ServerHTTP 实现 Handler 接口用于处理请求
func (e *Engine) ServeHTTP(writer http.ResponseWriter, request *http.Request) {
	e.router.handler(newContext(writer, request))
}

// Run 启动Web服务
func (e *Engine) Run(addr string) (err error) {
	return http.ListenAndServe(addr, e)
}

// Get 设置 Get 请求路由
func (e *Engine) Get(pattern string, handleFunc HandleFunc) {
	e.router.addRouter("GET", pattern, handleFunc)
}

// Post 设置 Post 请求路由
func (e *Engine) Post(pattern string, handleFunc HandleFunc) {
	e.router.addRouter("POST", pattern, handleFunc)
}
