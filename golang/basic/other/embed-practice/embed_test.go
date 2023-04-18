package embed_practice

import (
	"embed"
	_ "embed"
	"html/template"
	"net/http"
	"testing"
)

// embed 是 go1.16 的新特性，可以使用 //go:embed 注释，可以在编译阶段将静态资源文件打包进行编译好的程序中，并提供访问
// embed 可以将静态文件嵌入到三种类型中 string、字节数组、embed.FS

// 字符串类型
//
//go:embed embed_test.go
var embedString string

func TestEmbedString(t *testing.T) {
	t.Log(embedString)
}

// 字节数组类型
//
//go:embed embed_test.go
var embedByteArr []byte

func TestEmbedByteArr(t *testing.T) {
	t.Log(embedByteArr)
}

// embed.FS类型
//
//go:embed static
var embedFS embed.FS

func TestEmbedFS(t *testing.T) {

	// 读取文件目录
	dirs, err := embedFS.ReadDir("static")
	if err != nil {
		panic(err)
	}

	for _, dir := range dirs {
		t.Log(dir.Name())
	}

	// 打开要读取的文件
	file, err := embedFS.Open("static/js/index.js")
	if err != nil {
		panic(err)
	}
	t.Log(file.Stat())

	// 读取文件内容
	bytes, err := embedFS.ReadFile("static/js/index.js")
	if err != nil {
		panic(err)
	}
	t.Log(string(bytes))
}

func TestHttpServer(t *testing.T) {
	_ = http.ListenAndServe(":9999", http.FileServer(http.FS(embedFS)))
}

//go:embed static/templates
var templates embed.FS

func TestTemplate(t *testing.T) {
	tmpl, _ := template.ParseFS(templates, "static/templates/*.html")
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		_ = tmpl.ExecuteTemplate(w, "index.html", map[string]interface{}{
			"title": "Golang",
		})
	})
	_ = http.ListenAndServe(":9999", nil)
}
