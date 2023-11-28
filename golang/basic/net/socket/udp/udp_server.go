package main

import (
	"fmt"
	"net"
)

func main() {
	addr, err := net.ResolveUDPAddr("udp", "0.0.0.0:10001")
	if err != nil {
		panic(err)
	}

	conn, err := net.ListenUDP("udp", addr)
	if err != nil {
		panic(err)
	}
	defer conn.Close()
	fmt.Printf("Connecting to:%s\n", conn.LocalAddr())

	buffer := make([]byte, 1024)
	for {
		n, _, err := conn.ReadFrom(buffer)
		if err != nil {
			fmt.Printf("err =>  %s\n", err.Error())
			return
		}
		fmt.Printf("%s\n", string(buffer[:n]))
	}
}
