package main

import (
	"github.com/SilenceHVK/blog/golang/go-kit/endpoint"
	"github.com/SilenceHVK/blog/golang/go-kit/service"
	"github.com/SilenceHVK/blog/golang/go-kit/transport"
	"github.com/SilenceHVK/blog/golang/go-kit/utils"
	httptransport "github.com/go-kit/kit/transport/http"
	"github.com/gorilla/mux"
	"net/http"
)

func main() {
	userService := service.UserService{}
	serviceEndpoint := endpoint.GenUserServiceEndpoint(userService)

	r := mux.NewRouter()
	r.Methods(http.MethodGet).Path("/user/{uid:[0-9]+}").Handler(httptransport.NewServer(serviceEndpoint, transport.DecodeUserServiceRequest, transport.EncodeUserServiceResponse))

	utils.RegisterService()
	http.ListenAndServe(":9999", r)
}
