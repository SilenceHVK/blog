package utils

import consul "github.com/hashicorp/consul/api"

var consulClient *consul.Client

func init() {
	// 指定 Consul 注册中心地址
	config := consul.DefaultConfig()
	config.Address = "192.168.50.130:55001"

	client, err := consul.NewClient(config)
	if err != nil {
		panic(err)
	}
	consulClient = client
}

func RegisterService() {
	//  指定服务注册信息
	registration := &consul.AgentServiceRegistration{
		ID:      "USER-SERVICE",
		Name:    "USER_SERVICE",
		Address: "192.168.50.130",
		Port:    9999,
	}

	_ = consulClient.Agent().ServiceRegister(registration)
}

func UnRegister() {
	_ = consulClient.Agent().ServiceDeregister("USER-SERVICE")
}
