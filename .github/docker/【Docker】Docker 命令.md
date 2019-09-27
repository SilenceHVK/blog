## 导出 docker images

```bash
$ docker save -o path.tar imageName
```

## 导入 docker images

```bash
$ docker load < image.tar
```

## 监控容器资源消耗

```bash
$ docker stats [Options] [containerID/containerName]
```

默认情况下，stats 命令会每隔 1s 刷新输出

- [CONTAINER]：以短格式显示容器的 ID。
- [CPU %]：CPU 的使用情况。
- [MEM USAGE / LIMIT]：当前使用的内存和最大可以使用的内存。
- [MEM %]：以百分比的形式显示内存使用情况。
- [NET I/O]：网络 I/O 数据。
- [BLOCK I/O]：磁盘 I/O 数据。
- [PIDS]：PID 号。

Options

> --no-stream 只返回当前的状态

> --format 格式化输出结果

```bash
$ docker stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

- .Container： 根据用户指定的名称显示容器的名称或 ID。
- .Name： 容器名称。
- .ID： 容器 ID。
- .CPUPerc： CPU 使用率。
- .MemUsage： 内存使用量。
- .NetIO： 网络 I/O。
- .BlockIO： 磁盘 I/O。
- .MemPerc： 内存使用率。
- .PIDs： PID 号。
