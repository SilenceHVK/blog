package dto

import "time"

type User struct {
	/*
		binding:  两个值同时满足使用 , 分割，任意一个条件满足使用 | 分割
		  "required" 必填项
		gin 的参数验证规则使用的是 golang 的验证方法
	*/
	Name      string    `form:"name" binding:"required"`
	Age       int       `form:"age"`
	Address   string    `form:"address"`
	Birthday  time.Time `form:"birthday" time_format:"2006-01-02"`
	LoginTime time.Time `form:"loginTime" binding:"required" time_format:"2006-01-02"`
}
