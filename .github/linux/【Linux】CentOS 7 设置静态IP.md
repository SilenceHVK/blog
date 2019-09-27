1. 验证网络管理器服务的状态

```bash
$ systemctl status NetworkManager.service
```

2. 检查受网络管理器管理的网络接口

```bash
$ nmcli dev status
```

3. 进入 `/etc/sysconfig/network-scripts` 文件，找到配置文件

```
# 设置为静态
BOOTPROTO=static
# 定义 IP 地址
IPADDR=192.168.129.159
# 定义网卡
NETMASK=255.255.255.0
# 定义网关
GATEWAY=172.16.79.2
# 接口将通过该配置文件进行设置
NM_CONTROLLED=no
# 启动时开启该接口
ONBOOT=yes
# 设置 DNS
DNS1=172.16.79.2
```

4. 编辑 /etc/resolv.conf

```
nameserver 0.0.0.0
nameserver 114.114.114.114
search localhost
```

5. 重启 `network` 服务

```bash
$ systemctl restart network.service
```
