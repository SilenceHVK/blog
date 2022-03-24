package main

import (
	"log"
	"os"
	"os/signal"
	"syscall"
)

// Signal（信号）是 Linux，类Unix 和其他 POSIX 兼容的操作系统中用来进程间通讯的一种方式，
// 一个信号就是一个异步的通知，发送给某个进程，或同进程的某个线程，告诉它们某个事件发生了。

// Go 信号通知机制可以通过往一个 channel 中发送 os.Signal 实现
func main() {
	// 创建 os.Signal channel
	signalChannel := make(chan os.Signal)

	// 注册接收的信号，SIGINT = ctrl + c, SIGTERM = 程序终止
	signal.Notify(signalChannel, syscall.SIGINT, syscall.SIGTERM)

	done := make(chan bool)

	go func() {
		log.Println(<-signalChannel)
		done <- true
	}()

	log.Println("程序执行中.....")
	<-done // 程序在此阻塞，直至 done channel 中有值
	log.Println("程序执行结束.")
}
