package me.hvkcoder.java_basic.io.socket.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author h_vk
 * @since 2021/6/30
 */
@Slf4j
public class UDPClient {
  public static void main(String[] args) throws IOException {
    // 创建 packet 数据包
    byte[] data = "Hello, 客户端".getBytes();
    DatagramPacket packet = new DatagramPacket(data, data.length, new InetSocketAddress("127.0.0.1", 9999));
    // 将客户端数据报发送给服务器端
    DatagramSocket datagramSocket = new DatagramSocket();
    datagramSocket.send(packet);
    //
  }
}
