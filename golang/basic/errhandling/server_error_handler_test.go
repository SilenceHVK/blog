package errhandling

import (
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strings"
	"testing"
)

type appHandler func(writer http.ResponseWriter, request *http.Request) error

// 定义用户错误接口
type userError interface {
	error
	Message() string
}

// 自定义用户错误类型，并实现用户错误接口
type userErr string

func (e userErr) Error() string {
	return e.Message()
}
func (e userErr) Message() string {
	return string(e)
}

// 定义服务错误统一处理函数
var errorWrapper = func(handler appHandler) func(writer http.ResponseWriter, request *http.Request) {
	return func(writer http.ResponseWriter, request *http.Request) {

		// 使用 recover 捕获异常
		defer func() {
			if r := recover(); r != nil {
				log.Print(r)
				http.Error(writer, http.StatusText(http.StatusInternalServerError), http.StatusInternalServerError)
			}
		}()

		err := handler(writer, request)
		if err != nil {
			// err 为 用户自定义错误
			if userErr, ok := err.(userError); ok {
				http.Error(writer, userErr.Message(), http.StatusBadRequest)
				return
			}
			stateCode := http.StatusOK
			switch {
			case os.IsNotExist(err):
				stateCode = http.StatusNotFound
			case os.IsPermission(err):
				stateCode = http.StatusForbidden
			default:
				stateCode = http.StatusInternalServerError
			}
			http.Error(writer, http.StatusText(stateCode), stateCode)
		}
	}
}

const PREFIX = "/list/"

func fileListHandler(writer http.ResponseWriter, request *http.Request) error {

	if strings.Index(request.URL.Path, PREFIX) != 0 {
		return userErr("path must start with " + PREFIX)
	}

	path := request.URL.Path[len("/list/"):]
	file, err := os.Open(path)
	defer file.Close()
	if err != nil {
		return err
	}
	reader, err := ioutil.ReadAll(file)
	if err != nil {
		return err
	}
	writer.Write(reader)
	return nil
}

func TestFileServer(t *testing.T) {
	http.HandleFunc("/", errorWrapper(fileListHandler))
	err := http.ListenAndServe(":8888", nil)
	if err != nil {
		panic(err)
	}
}
