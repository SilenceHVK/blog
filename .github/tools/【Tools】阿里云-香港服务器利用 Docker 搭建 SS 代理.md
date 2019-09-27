1. 下拉 docker 镜像

```bash
docker pull mritd/shadowsocks
```

2. 运行 shadowsocks 镜像

```bash
docker run -dt --name ss-server -p 6445:6445 mritd/shadowsocks -s "-s 0.0.0.0 -p 6445 -m aes-256-cfb -k 123456test --fast-open" --restart=always
```

3. 屏蔽阿里云盾扫描 IP

```bash
#!/bin/bash
echo "屏蔽阿里云盾恶意 IP......."
iptables -I INPUT -s 140.205.201.0/28 -j DROP
iptables -I INPUT -s 140.205.201.16/29 -j DROP
iptables -I INPUT -s 140.205.201.32/28 -j DROP
iptables -I INPUT -s 140.205.225.192/29 -j DROP
iptables -I INPUT -s 140.205.225.200/30 -j DROP
iptables -I INPUT -s 140.205.225.184/29 -j DROP
iptables -I INPUT -s 140.205.225.183/32 -j DROP
iptables -I INPUT -s 140.205.225.206/32 -j DROP
iptables -I INPUT -s 140.205.225.205/32 -j DROP
iptables -I INPUT -s 140.205.225.195/32 -j DROP
iptables -I INPUT -s 140.205.225.204/32 -j DROP
iptables -I INPUT -s 106.11.224.0/26 -j DROP
iptables -I INPUT -s 106.11.224.64/26 -j DROP
iptables -I INPUT -s 106.11.224.128/26 -j DROP
iptables -I INPUT -s 106.11.224.192/26 -j DROP
iptables -I INPUT -s 106.11.222.64/26 -j DROP
iptables -I INPUT -s 106.11.222.128/26 -j DROP
iptables -I INPUT -s 106.11.222.192/26 -j DROP
iptables -I INPUT -s 106.11.223.0/26 -j DROP
echo "已经屏蔽了阿里云盾恶意 IP"
```

4. 使用 shadowsocks 客户端连接
