package web_framework

import (
	"encoding/json"
	"fmt"
	"net/http"
)

type H map[string]interface{}

type Context struct {
	Writer  http.ResponseWriter
	Request *http.Request
	Method  string
	Path    string
}

// newContext 实例 Context
func newContext(write http.ResponseWriter, req *http.Request) *Context {
	return &Context{
		Writer:  write,
		Request: req,
		Method:  req.Method,
		Path:    req.URL.Path,
	}
}

// SetHeader 设置响应头
func (c *Context) SetHeader(key, value string) {
	c.Writer.Header().Set(key, value)
}

// SetStatus 设置状态码
func (c *Context) SetStatus(statusCode int) {
	c.Writer.WriteHeader(statusCode)
}

// Query 获取 URL 请求参数
func (c *Context) Query(name string) string {
	return c.Request.URL.Query().Get(name)
}

// FormValue 获取表单参数
func (c *Context) FormValue(name string) string {
	return c.Request.FormValue(name)
}

// String 设置String返回类型
func (c *Context) String(statusCode int, format string, values ...interface{}) {
	c.SetStatus(statusCode)
	c.SetHeader("Content-Type", "text/plain")
	c.Writer.Write([]byte(fmt.Sprintf(format, values...)))
}

// HTML 设置HTML返回类型
func (c *Context) HTML(statusCode int, format string, values ...interface{}) {
	c.SetStatus(statusCode)
	c.SetHeader("Content-Type", "html/plain")
	c.Writer.Write([]byte(fmt.Sprintf(format, values...)))
}

// JSON 设置JSON返回类型
func (c *Context) JSON(statusCode int, value interface{}) {
	c.SetStatus(statusCode)
	c.SetHeader("Content-Type", "application/json")
	if err := json.NewEncoder(c.Writer).Encode(value); err != nil {
		http.Error(c.Writer, err.Error(), http.StatusInternalServerError)
	}
}
