package main

import (
	web_framework "github.com/SilenceHVK/blog/golang/custom/web-framework"
	"net/http"
)

func main() {
	engine := web_framework.New()

	engine.Get("/", func(c *web_framework.Context) {
		c.String(http.StatusOK, "Hello World")
	})

	engine.Get("/user", func(c *web_framework.Context) {
		c.HTML(http.StatusOK, "<h1>Name: %s, Age: %s<h1>", c.Query("name"), c.Query("age"))
	})

	engine.Post("/login", func(c *web_framework.Context) {
		c.JSON(http.StatusOK, web_framework.H{
			"username": c.FormValue("username"),
			"password": c.FormValue("password"),
		})
	})

	engine.Run(":9999")
}
