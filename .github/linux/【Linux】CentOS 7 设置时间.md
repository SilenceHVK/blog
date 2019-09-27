## 1.设置系统时间为中国时区并启用 NTP 同步

```bash
yum install -y ntp //安装ntp服务

systemctl enable ntpd //开机启动服务

systemctl start ntpd //启动服务

timedatectl set-timezone Asia/Shanghai //更改时区

timedatectl set-ntp yes //启用ntp同步

ntpq -p //同步时间
```

## 2.timedatectl 命令

```bash
## 读取时间
timedatectl  //等同于 timedatectl status

## 列出所有时区
timedatectl list-timezones

## 设置时区
timedatectl set-timezone Asia/Shanghai

## 设置是否与NTP服务器同步
timedatectl set-ntp yes  //yes或者no

## 将硬件时钟调整为与本地时钟一致
hwclock --systohc --localtime 或 timedatectl set-local-rtc 1

## 将硬件时间设置成 UTC
hwclock --systohc --utc 或 timedatectl set-local-rtc 1
```

## 3.时钟概念

在 CentOS 6 版本，时间设置有 date、hwclock 命令，从 CentOS 7 开始，使用了一个新的命令 timedatectl。

- UTC

&ensp;&ensp; 整个地球分为二十四时区，每个时区都有自己的本地时间。在国际无线电通信场合，为了统一起见，使用一个统一的时间，称为通用协调时(UTC, Universal Time Coordinated)。

- GMT

&ensp;&ensp; 格林威治标准时间 (Greenwich Mean Time)指位于英国伦敦郊区的皇家格林尼治天文台的标准时间，因为本初子午线被定义在通过那里的经线。(UTC 与 GMT 时间基本相同，本文中不做区分)

- CST

&ensp;&ensp; 中国标准时间 (China Standard Time)【GMT + 8 = UTC + 8 = CST】

- DST

&ensp;&ensp; 夏令时(Daylight Saving Time) 指在夏天太阳升起的比较早时，将时钟拨快一小时，以提早日光的使用。（中国不使用）

硬件时钟：

&ensp;&ensp;RTC(Real-Time Clock)或 CMOS 时钟，一般在主板上靠电池供电，服务器断电后也会继续运行。仅保存日期时间数值，无法保存时区和夏令时设置。

系统时钟：

&ensp;&ensp;一般在服务器启动时复制 RTC 时间，之后独立运行，保存了时间、时区和夏令时设置。
