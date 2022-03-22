package main

import (
	"github.com/SilenceHVK/blog/golang/go-kit/endpoint"
	"github.com/SilenceHVK/blog/golang/go-kit/service"
	"github.com/SilenceHVK/blog/golang/go-kit/transport"
	httptransport "github.com/go-kit/kit/transport/http"
	"net/http"
)

func main() {
	userService := service.UserService{}
	serviceEndpoint := endpoint.GenUserServiceEndpoint(userService)

	server := httptransport.NewServer(serviceEndpoint, transport.DecodeUserServiceRequest, transport.EncodeUserServiceResponse)
	http.ListenAndServe(":9999", server)
}
