// Package transport 层负责接受用户请求并将数据转为 Endpoint 使用的数据格式，并把 Endpoint 的返回值封装返回给用户
package transport

import (
	"context"
	"encoding/json"
	"errors"
	"github.com/SilenceHVK/blog/golang/go-kit/endpoint"
	"github.com/gorilla/mux"
	"net/http"
	"strconv"
)

// DecodeUserServiceRequest 对用户服务请求解码
func DecodeUserServiceRequest(ctx context.Context, request *http.Request) (interface{}, error) {
	vars := mux.Vars(request)
	if uid, ok := vars["uid"]; ok {
		id, _ := strconv.Atoi(uid)
		return endpoint.UserRequest{
			Uid: id,
		}, nil
	}
	return nil, errors.New("参数未传递")
}

// EncodeUserServiceResponse 对用户请求服务响应
func EncodeUserServiceResponse(ctx context.Context, writer http.ResponseWriter, response interface{}) error {
	return json.NewEncoder(writer).Encode(response)
}
