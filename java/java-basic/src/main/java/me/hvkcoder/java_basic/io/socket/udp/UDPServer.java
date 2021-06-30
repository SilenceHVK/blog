package me.hvkcoder.java_basic.io.socket.udp;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author h_vk
 * @since 2021/6/30
 */
@Slf4j
public class UDPServer {
  public static void main(String[] args) throws IOException {
    // 创建 DatagramSocket
    DatagramSocket datagramSocket = new DatagramSocket(9999);
    // 创建 packet 对象，用于接收客户端数据
    byte[] data = new byte[1024];
    DatagramPacket packet = new DatagramPacket(data, data.length);
    // 接收客户端数据，在未接收到客户端数据时该方法将会阻塞
    datagramSocket.receive(packet);
    log.info("客户端：{}", new String(data, 0, packet.getLength()));
  }
}
