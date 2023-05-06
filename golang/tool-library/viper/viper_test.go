package main

import (
	"github.com/SilenceHVK/blog/golang/tool-library/viper/conf"
	"testing"
)

func TestPrintConfig(t *testing.T) {
	t.Log(conf.ServerConf)
}
