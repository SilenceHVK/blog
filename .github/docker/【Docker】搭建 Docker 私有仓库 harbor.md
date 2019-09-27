1. 安装 `docker-compose`

```bash
curl -L https://get.daocloud.io/docker/compose/releases/download/1.22.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

chmod +x /usr/local/bin/docker-compose
```

2. 安装 `docker`

```bash
yum install -y docker
```

3. 配置 `daemon.json`

```bash
cat > /etc/docker/daemon.json >> EOF
{
    "registry-mirrors": ["https://hub-mirror.c.163.com", "https://docker.mirrors.ustc.edu.cn"],
    // 不安全的注册表
    "insecure-registries": ["harbor ip地址"],
    "max-concurrent-downloads": 20
}
EOF
```

4. 启动 `docker`

```bash
systemctl daemon-reload
systemctl restart docker
```

5. 下载 `harbor`

```bash
wget https://storage.googleapis.com/harbor-releases/release-1.6.0/harbor-online-installer-v1.6.0.tgz

tar -zxvf harbor-online-installer-v1.6.0.tgz
```

6. 安装 `harbor`

- 修改 `harbor.cfg`

```config
hostname = // harbor ip 地址
```

- 执行安装

```bash
source install.sh   --with-clair
```

7. 在浏览器中输入 harbor ip 地址，用户名: admin，密码：Harbor12345

![harbor](https://github.com/SilenceHVK/Articles/raw/master/assets/articles-img/docker/harbor.png)
