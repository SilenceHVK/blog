package web_framework

import "net/http"

// HandleFunc 定义处理函数类型
type HandleFunc func(c *Context)

type router struct {
	handlers map[string]HandleFunc
}

// newRouter 实例化 router
func newRouter() *router {
	return &router{
		handlers: make(map[string]HandleFunc),
	}
}

// handler 处理请求路由
func (r *router) handler(c *Context) {
	if handleFunc, ok := r.handlers[c.Method+"-"+c.Path]; ok {
		handleFunc(c)
	} else {
		c.HTML(http.StatusNotFound, "<h1>Not Found：%q</h1>", c.Path)
	}
}

// addRouter 添加路由
func (r *router) addRouter(method, pattern string, handleFunc HandleFunc) {
	r.handlers[method+"-"+pattern] = handleFunc
}
