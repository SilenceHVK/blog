package main

import (
	"fmt"
	"github.com/SilenceHVK/blog/golang/go-kit/endpoint"
	"github.com/SilenceHVK/blog/golang/go-kit/service"
	"github.com/SilenceHVK/blog/golang/go-kit/transport"
	"github.com/SilenceHVK/blog/golang/go-kit/utils"
	httptransport "github.com/go-kit/kit/transport/http"
	"github.com/gorilla/mux"
	"log"
	"net/http"
	"os"
	"os/signal"
	"syscall"
)

func main() {
	userService := service.UserService{}
	serviceEndpoint := endpoint.GenUserServiceEndpoint(userService)

	r := mux.NewRouter()
	r.Methods(http.MethodGet).Path("/user/{uid:[0-9]+}").Handler(httptransport.NewServer(serviceEndpoint, transport.DecodeUserServiceRequest, transport.EncodeUserServiceResponse))

	errChan := make(chan error)
	go func() {
		utils.RegisterService()
		err := http.ListenAndServe(":9999", r)
		if err != nil {
			log.Println(err)
			errChan <- err
		}
	}()

	go func() {
		// 监听服务信号
		sigC := make(chan os.Signal)
		signal.Notify(sigC, syscall.SIGINT, syscall.SIGTERM)
		errChan <- fmt.Errorf("%s", <-sigC)
	}()

	getError := <-errChan
	utils.UnRegister()
	log.Println(getError)
}
