package utils

import (
	"testing"
	"time"
)

/*
	time 包提供了时间的显示和测量用的函数，日历的计算采用的是公历
*/

func TestTime(t *testing.T) {
	now := time.Now()
	t.Log("当前时间：", now)
	t.Log("本地时区时间：", now.Local())
	t.Log("国际统一时间：", now.UTC())
	t.Log("时间戳（秒）：", now.Unix())
	t.Log("时间戳（纳秒）：", now.UnixNano())

	year, month, day := now.Date()
	t.Logf("返回对应的日期：%d 年 %d 月 %d 日\n", year, month, day)
	t.Log("返回 年：", now.Year())
	t.Log("返回 月：", now.Month())
	t.Log("返回 日：", now.Day())
	t.Log("返回 时：", now.Hour())
	t.Log("返回 分：", now.Minute())
	t.Log("返回 秒：", now.Second())
	t.Log("返回 纳秒：", now.Nanosecond())
	t.Log("返回 星期：", now.Weekday())

	hour, min, sec := now.Clock()
	t.Logf("返回对应的小时：%d时%d分%d秒\n", hour, min, sec)

	// 根据指定数值返回一个 Time 对象
	date := time.Date(2020, 12, 12, 0, 0, 0, 0, time.UTC)
	t.Log("自定义日期：", date)

	// 将字符串转为 time 对象
	parse, err := time.Parse("2006-01-02 15:04:05", "2020-12-12 00:00:00")
	if err != nil {
		panic(err)
	}
	t.Log("字符串转 time：", parse)

	t.Logf("%s 与 %s 时间相等 %v \n", now, date, now.Equal(date))
	t.Logf("%s 与 %s 时间之前 %v \n", now, date, now.Before(date))
	t.Logf("%s 与 %s 时间之后 %v \n", now, date, now.After(date))
	t.Logf("%s 与 %s 时间差 %v \n", now, date, now.Sub(date))

	t.Log("一年一个月零一天之后的日期：", now.AddDate(1, 1, 1))

	// 解析一个时间段字符串，单位 ns, ms, s, m, h
	duration, _ := time.ParseDuration("1h30m")
	t.Log("返回一个半小时后的日期：", now.Add(duration))
}
