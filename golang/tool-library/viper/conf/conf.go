package conf

import (
	"fmt"
	"github.com/spf13/viper"
)

var (
	ServerConf = &server{
		Address: "127.0.0.1",
		Port:    8080,
	}
)

func init() {
	viper.AddConfigPath("conf")
	viper.SetConfigName("config")
	viper.SetConfigType("yaml")

	// 查找并读取配置文件
	if err := viper.ReadInConfig(); err != nil {
		if _, ok := err.(viper.ConfigFileNotFoundError); ok {
			panic(fmt.Errorf("配置文件未找到：%s\n", err))
		} else {
			panic(fmt.Errorf("读取配置文件错误：%s\n", err))
		}
	}
	unmarshalKey("server", &ServerConf)
}

// 解析映射配置文件
func unmarshalKey(key string, conf interface{}) {
	if err := viper.UnmarshalKey(key, conf); err != nil {
		panic(fmt.Errorf("解析 %s 配置映射失败: %s\n", key, err))
	}
}
