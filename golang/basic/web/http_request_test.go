package web

import (
	"bufio"
	"bytes"
	"encoding/json"
	"fmt"
	"io"
	"io/ioutil"
	"net/http"
	"os"
	"path"
	"testing"
	"time"
)

func TestGet(t *testing.T) {
	imageUrl := "http://127.0.0.1:5500/train/ants/1095476100_3906d8afde.jpg"
	fileName := path.Base(imageUrl)
	resp, err := http.Get(imageUrl)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()

	writer, _ := os.Create(fileName)
	reader := bufio.NewReader(resp.Body)
	io.Copy(writer, reader)
}

func TestPost(t *testing.T) {
	var params interface{}
	body, _ := json.Marshal(params)
	request, _ := http.NewRequest(http.MethodPost, "", bytes.NewBuffer(body))
	request.Header.Set("Content-Type", "application/json")

	client := &http.Client{
		// 设置超时时间
		Timeout: time.Second * time.Duration(10),
	}
	resp, err := client.Do(request)
	if err != nil {
		panic(err)
	}
	defer resp.Body.Close()
	if resp.StatusCode == 200 {
		bodyBytes, err := ioutil.ReadAll(resp.Body)
		if err != nil {
			panic(err)
		}
		fmt.Println(string(bodyBytes))
	}
}
