package main

import "net"

func main() {
	addr, err := net.ResolveUDPAddr("udp", "127.0.0.1:10001")
	if err != nil {
		panic(err)
	}

	udp, err := net.DialUDP("udp", nil, addr)
	if err != nil {
		panic(err)
	}
	defer udp.Close()

	_, _ = udp.Write([]byte("Hello World"))
}
