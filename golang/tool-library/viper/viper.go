package main

import (
	"fmt"
	"github.com/fsnotify/fsnotify"
	"github.com/spf13/viper"
)

type Server struct {
	Address string
	Port    int64
	enable  bool // 小写属性 viper 将不做匹配
}

// Viper 用于读取配置文件的工具库
func main() {
	// 添加配置文件路径
	viper.AddConfigPath("viper/conf")
	// 设置配置文件名称
	viper.SetConfigName("config")
	// 设置配置文件类型，viper 支持 JSON, TOML, YAML, HCL, envfile 和 Java 程序的 properties
	viper.SetConfigType("yaml")
	// 查找并读取配置文件
	if err := viper.ReadInConfig(); err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); ok {
			panic(fmt.Errorf("配置文件未找到：%s\n", err))
		} else {
			panic(fmt.Errorf("读取配置文件错误：%s\n", err))
		}
	}

	// 根据 key 读取不同类型的值
	fmt.Println(viper.Get("server"))
	fmt.Println(viper.GetString("server.address"))
	fmt.Println(viper.GetInt64("server.port"))
	fmt.Println(viper.GetBool("server.enable"))

	// 将读取的配置映射至结构体
	server := &Server{}
	if err := viper.UnmarshalKey("server", server); err != nil {
		panic(fmt.Errorf("映射结构体失败：%s\n", err))
	}
	fmt.Println(server)

	// 当配置文件发生变化时执行的操作
	viper.OnConfigChange(func(in fsnotify.Event) {
		fmt.Printf("配置文件发生更改: %s\n", in.Name)
	})
	// 设置配置文件监听
	viper.WatchConfig()
}
