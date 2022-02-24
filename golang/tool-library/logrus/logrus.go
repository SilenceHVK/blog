package main

import (
	"bytes"
	"fmt"
	"github.com/sirupsen/logrus"
	"io"
	"os"
)

type MyLogFormatter struct {
}

func (m *MyLogFormatter) Format(entry *logrus.Entry) ([]byte, error) {
	var data *bytes.Buffer

	// 当 entry.log 调用 被调用时，日志信息将缓冲到 Buffer 中
	if entry.Buffer != nil {
		data = entry.Buffer
	} else {
		data = &bytes.Buffer{}
	}

	// 设置日志日期格式
	time := entry.Time.Format("2006-01-02 15:04:05")

	var appendLog string
	for key, value := range entry.Data {
		appendLog += fmt.Sprintf("%s:%v ", key, value)
	}
	// 设置日志格式
	log := fmt.Sprintf("【%s】【%s】【%s:%d】=> %s %s\n", entry.Level, time, entry.Caller.File, entry.Caller.Line, entry.Message, appendLog)
	data.WriteString(log)
	return data.Bytes(), nil
}

func main() {
	/*
			设置日志级别，高于该级别的日志信息不会展示，日志级别由高到低依次为：
		  PanicLevel
			FatalLevel
			ErrorLevel
			WarnLevel
			InfoLevel
			DebugLevel
			TraceLevel
	*/
	logrus.SetLevel(logrus.TraceLevel)

	// 是否显示调用信息
	logrus.SetReportCaller(true)

	// 日志重定向
	logFile, err := os.OpenFile("logrus.log", os.O_CREATE|os.O_WRONLY, 777)
	if err != nil {
		logrus.Error(err)
	}
	logrus.SetOutput(io.MultiWriter(logFile, os.Stdout))

	// 自定义日志格式，设置 Text 格式
	//logrus.SetFormatter(&logrus.TextFormatter{
	//	ForceQuote:      true,                  // 键值对加引号
	//	TimestampFormat: "2006-01-02 15:04:05", // 设置日志时间格式
	//})

	// 自定义日志格式，以 JSON 格式输出
	//logrus.SetFormatter(&logrus.JSONFormatter{
	//	TimestampFormat: "2006-01-02 15:04:05",
	//	PrettyPrint:     true,
	//})

	// 自定义日志格式，只需要传入实现 logrus.Formatter 接口的结构体，便可以实现自定义日志格式
	logrus.SetFormatter(&MyLogFormatter{})

	// 追加日志字段, logrus.Fields 本质就是 map[string]interface{}
	logrus.WithFields(logrus.Fields{
		"ip":      "127.0.0.1",
		"user_id": 12345,
	}).Info("This is test log.")

	//// 先输出日志，再执行 Panic
	//logrus.Panic("panic msg")
	//// 致命错误日志，输出日志后，程序将结束
	//logrus.Fatal("fatal msg")
}
