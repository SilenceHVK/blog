<img src="https://i.postimg.cc/15vK8cZ0/nginx.png" style="display:block;margin:30px auto;" alt="sendfile 模型图" />

[Nginx](http://nginx.org/) 是一个开源且高性能、可靠的 HTTP 中间件、代理服务。

## Nginx 特性

- I/O 多路复用 epoll

<img src="https://i.postimg.cc/MK6DZ3gK/multiplexing.png" style="display:block;margin:auto;" alt="I/O 多路复用模型图" />

> &ensp;&ensp;多个描述符的 I/O 操作都能在一个线程内并发交替地顺序完成，就叫做“ I/O 多路复用”。这里的“复用”是指同一个线程。

    I/O 多路复用使用的方式：

    select 模型

    1. 能够监视文件描述的数量存在最大限制；
    2. select 模型采用线性遍历方式，使得扫描效率低下；

    epoll 模型

    1. 最大连接无限制；
    2. 当每个 FD 就绪，采用系统的回调函数之间将 FD 放入，效率更高；

- 轻量级

  - 功能模块少；
  - 代码模块化；

- CPU 亲和(affinity)

> CPU 亲和是一种把 CPU 核心和 Nginx 工作进程绑定，把每个 worker 进程固定在一个 cpu 上执行，减少切换 cpu 的 cache miss，获得更好的性能。

- sendfile

> Nginx 在传递静态文件时，直接通过内核空间传递给 Socket ，响应给用户。

<img src="https://i.postimg.cc/Hkj9Xcpq/sendfile.png" style="display:block;margin:auto;" alt="sendfile 模型图" />

## Nginx 快速安装

- nginx 发行版本

  - Mainline version 开发版；
  - Stable version 稳定版；
  - Legacy version 历史版；

- RHEL/CentOS 通过 yum 安装

  1. 初始化系统

     ```
     ## 关闭防火墙
     sudo systemctl stop firewalld
     sudo systemctl disable firewalld
     sudo iptables -F && sudo iptables -X && sudo iptables -F -t nat && sudo iptables -X -t nat
     sudo sudo iptables -P FORWARD ACCEPT

     ## 关闭 SELinux
     sudo setenforce 0
     sudo sed -i 's/^SELINUX=.*/SELINUX=disabled/' /etc/selinux/config
     ```

  2. `sudo yum install yum-utils`；
  3. 创建文件 `/etc/yum.repos.d/nginx.repo` 并输入以下内容：

  ```
  [nginx-stable]
  name=nginx stable repo
  baseurl=http://nginx.org/packages/centos/$releasever/$basearch/
  gpgcheck=1
  enabled=1
  gpgkey=https://nginx.org/keys/nginx_signing.key

  [nginx-mainline]
  name=nginx mainline repo
  baseurl=http://nginx.org/packages/mainline/centos/$releasever/$basearch/
  gpgcheck=1
  enabled=0
  gpgkey=https://nginx.org/keys/nginx_signing.key
  ```

  默认情况下，yum 使用的是 nginx 稳定版库，如果要使用开发版库，可以执行命令 `sudo yum-config-manager --enable nginx-mainline`；

  4. `sudo yum install nginx`；
  5. `rpm -ql nginx` 查看 nginx 安装目录；

    <img src="https://i.postimg.cc/T3Sq9d1p/nginx-dir.png" style="display:block;margin:auto;" alt="nginx 安装目录 图片"/>

## Nginx 基础配置

&ensp;&ensp;`nginx` 默认配置路径 `/etc/nginx/nginx/conf`.

- 基础模块配置

```
user		        设置 nginx 服务的系统使用用户
worker_processes    工作进程数
error_log           nginx 的错误日志
pid                 nginx 服务启动时候的pid
```

- 事件模块

```
events {
    worker_connections   每个进程允许最大连接数
    use                  工作进程数
}
```

- http 协议模块配置

```
http {
  ## 每个 server 为每个独立的站点
  server {
     	listen    80;  ## 端口号
		server_name	localhost; ## 域名

		## 配置默认访问的路径配置
		location / {
			root  	页面根目录路径
			index   首页路径
		}

		## 错误页面配置
    	error_page   500 502 503 504  /50x.html;
    	location = /50x.html {
        	root   /usr/share/nginx/html;
    	}
  }
  server{
  }
}
```

## Nginx 的日志

- error.log

```
主要用于记录 nginx 每次 HTTP 请求的状态与自身服务运行的状态。
```

- access.log

```
主要用于记录 nginx 每次 HTTP 请求的响应状态。
```

**`error.log` 与 `access.log` 主要依赖于 `log_format` 的配置，`log_format` 只能配置在 http 模块下。**。

- log_format

```
普通格式配置

log_format main '$remote_addr - $remote_user [$time_local] $request '
                '"$status" $body_bytes_sent "$http_referer" '
                '"$http_user_agent" "$http_x_forwarded_for" "$request_time" "$upstream_response_time"';

```

```
json 格式配置

log_format logJson '{
                         "@timestamp": "$time_local", '
                         '"@fields": { '
                         '"remote_addr": "$remote_addr", '
                         '"remote_user": "$remote_user", '
                         '"body_bytes_sent": "$body_bytes_sent", '
                         '"request_time": "$request_time", '
                         '"status": "$status", '
                         '"request": "$request", '
                         '"request_method": "$request_method", '
                         '"http_referrer": "$http_referer", '
                         '"body_bytes_sent":"$body_bytes_sent", '
                         '"http_x_forwarded_for": "$http_x_forwarded_for", '
                         '"http_user_agent": "$http_user_agent" }
                    }';
```

| 字段                     | 说明                                       |
| ------------------------ | ------------------------------------------ |
| \$remote_addr            | 客户端地址                                 |
| \$remote_user            | 客户端用户名称                             |
| \$time_local             | 访问的时间和时区                           |
| \$request                | 请求的 URI 和 HTTP 协议                    |
| \$http_host              | 请求地址，即浏览器中输入的地址或域名       |
| \$status                 | HTTP 请求状态                              |
| \$upstream_status        | upstream 状态                              |
| \$body_bytes_sent        | 发送给客户端文件内容大小                   |
| \$http_referer           | url 跳转来源                               |
| \$http_user_agent        | 用户终端浏览器等信息                       |
| \$ssl_protocol           | SSL 协议版本                               |
| \$ssl_cipher             | 交换数据中的算法                           |
| \$upstream_addr          | 后台 upstream 地址，即真正提供服务主机地址 |
| \$request_time           | 整个请求的总时间                           |
| \$upstream_response_time | 请求过程中 upstream 的响应时间             |

指定日志格式

```
error_log  /var/log/nginx/error.log logJson buffer=32k;
access_log  /var/log/nginx/access.log  main buffer=32k;
```

## Nginx 中间件架构

- http_stub_status_module

```
用于监控 nginx 的运行状态，配置语法：

location /status {
    stub_status;
}

相关数据：

Active connections: 对后端发起的活动连接数
Server accepts handled requests:
Nginx 处理的连接个数 创建的握手次数 处理请求的个数

Reading: Nginx 读取到客户端的 Header 信息数
Writing: Nginx 返回客户端的 Header 信息数
Waiting: 开启 keep-alive 的情况下，这个值等于 active - (reading + writing)， 意思就是 Nginx 已经处理完成，正在等待下一次请求指令的驻留连接
```

- http_random_index_module

```
在目录中选择一个随机主页，但是不能选择隐藏文件，配置语法

location / {
    root 页面路径
    random_index on;
}
```

- http_sub_module

```
对 HTTP 内容替换

locatoin / {
    sub_filter 要替换的内容  要替换后的内容;

    # 用于设置网页内替换后是否有更新，主要用于缓存的场景
    sub_filter_last_modified on;

    # 字符串替换一次还是多次，默认替换一次，将其关闭为替换所有
    sub_filter_once no;
}
```

- Nginx 的请求限制

  - 连接频率限制 `limit_conn_module`
  - 请求频率限制 `limit_req_module`
