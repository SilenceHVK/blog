// Package endpoint 层负责定义 Request 和 Response 格式，并可以使用装饰器包装函数来实现各种中间件嵌套
package endpoint

import (
	"context"
	"github.com/SilenceHVK/blog/golang/go-kit/service"
	"github.com/go-kit/kit/endpoint"
)

type UserRequest struct {
	Uid int `json:"uid"`
}

type UserResponse struct {
	Result string `json:"result"`
}

// GenUserServiceEndpoint 生成 Endpoint
func GenUserServiceEndpoint(userService service.IUserService) endpoint.Endpoint {
	return func(ctx context.Context, request interface{}) (response interface{}, err error) {
		r := request.(UserRequest)
		return UserResponse{
			Result: userService.GetName(r.Uid),
		}, nil
	}
}
