## configuration

logback 根标签。

- scan：当配置文件如果发生改变时，将被重新加载，默认为 true。

- scanPeriod

        设置检测配置文件是否有修改的时间间隔，默认单位为毫秒，默认时间间隔为 1 分钟，只有 scan 为 true 时，该属性生效

- debug

        此属性为 true 时，将打印 logback 内部日志信息，实时查看 logback 运行状态。默认为 false

configuration 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <!-- 其他配置省略-->
</configuration>
```

## contextName

每个 logger 都关联到 logger 上下文，默认值为 `default`。但可使用 `<contextName>` 设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。

contextName 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <contextName>myAppName</contextName>
    <!-- 其他配置省略-->
</configuration>
```

## property

用来定义变量值的标签。

- name：变量名称；
- value：变量值；

可以使用 `${}` 使用变量。

property 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <property name="APP_NAME" value="myAppName"/>
    <contextName>${APP_NAME}</contextName>
    <!-- 其他配置省略-->
</configuration>
```

## timestamp

获取时间戳字符串。

- key：标识刺 `timestamp` 的名字；
- datePattern：时间戳格式，遵循 `java.txt.SimpleDateFormat` 的格式；

timestamp 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <timestamp name="bySecond" datePattern="yyyyMMdd'T'HHmmss"/>
    <contextName>${bySecond}</contextName>
    <!-- 其他配置省略-->
</configuration>
```

## loger

用来设置某一个包或者具体的某一个类的日志打印级别，以及指定 `<appender>`。

- name：用来指受此 `loger` 约束的某一个包或者具体的某一个类。
- level：用来设置打印级别，大小写无关 `TRACE`、`DEBUG`、`INFO`、`WARN`、`ERROR`、`ALL` 和 `OFF`

        还有个特殊值 INHERITED 或者同义词 NULL,代表强制执行上级的级别。如果未设置此属性，则当前 loger 会继承上级的级别。

- addtivity：是否向上级 loger 传递打印信息 默认为 true。

loger 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"></appender>

    <logers>
        <loger name="logback.LogbackDemo" level="DEBUG" addtivity="false">
            <appender-ref ref="STDOUT" />
        </loger>
    </logers>
    <!-- 其他配置省略-->
</configuration>
```

## root

也是 `<loger>` 元素，但是它是根`loger`，只有 `level` 属性，默认值为 `DEBUG`。

root 的配置

```xml
<?xml version="1.0" encoding="utf-8" ?>
<configuration scan="false" scanPeriod="60 seconds" debug="false">
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"></appender>

    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
    <!-- 其他配置省略-->
</configuration>
```

## appender

configuration 的子节点，主要负责写日志组件；

- name：指定 `appender` 日志组件名称。
- class：指定 `appender` 的全限定名。

  - ConsoleAppender 表示控制台输出

    - encoder：对日志进行格式化。

    ```xml
    <encoder>
        <pattern>%-5level | %date{yyyy-MM-dd HH:mm:ss} | %thread | %logger | %msg%n</pattern>
    </encoder>
    ```

  - FileAppender 将日志添加到文件中

    - file：日志名称，可以是相对路径，也可以是绝对路径
    - append：如果为 `true`。 当前日志内容追加到日志文件结尾，否则清空后重新添加，默认值为 `true`。
    - encoder：对日志进行格式化。
    - prudent：如果 `true`，日志会被安全的写入文件，即使其他的 `FileAppender` 也在此文件做了写入操作，效率低，默认值为 `false`。

    ```xml
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>log/logname.log</file>
        <append>true</append>
        <encoder>
            <pattern>%-5level | %date{yyyy-MM-dd HH:mm:ss} | %thread | %logger | %msg%n</pattern>
        </encoder>
        <prudent>false</prudent>
    </appender>
    ```

  - RollingFileAppender 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件。

    - rollingPolicy：当发生滚动时，决定 `RollingFileAppender` 的行为，涉及文件移动和重命名。

    - triggeringPolicy：告知 `RollingFileAppender` 合适激活滚动。
    - prudent：当为 `true` 时，不支持 `FixedWindowRollingPolicy`。支持 `TimeBasedRollingPolicy`，但是有两个限制，1 不支持也不允许文件压缩，2 不能设置 `file` 属性，必须留空

### rollingPolicy

- TimeBasedRollingPolicy

  最常用的滚动策略，根据时间里制定滚动策略，既负责滚动也负责触发滚动。

  - fileNamePattern：必要节点，包含文件名及 “%d” 转换符。
  - maxHistory：可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件。

  ```xml
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
          <fileNamePattern>log/log.%d{yyyy-MM-dd}.log</fileNamePattern>
      </rollingPolicy>

      <encoder>
          <pattern>${LOG_PATTERN}</pattern>
      </encoder>
  </appender>
  ```

- FixedWindowRollingPolicy

  根据固定窗口算法重命名文件的滚动策略。

  - minIndex：窗口索引最小值。
  - maxIndex：窗口索引最大值，当用户指定的窗口过大时，会自动将窗口设置为 12。
  - fileNamePattern：必须包含 “%d” ，假设最小值和最大值分别为 1 和 2，命名模式为 `log%i.log`，会产生归档文件 `log1.log` 和 `log2.log`。

  ```xml
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
          <rollingPolicy class="ch.qos.logback.core.rolling.FixedWindowRollingPolicy">
              <minIndex>1</minIndex>
              <maxIndex>2</maxIndex>
              <fileNamePattern>log/log%i.log</fileNamePattern>
          </rollingPolicy>
    </appender>
  ```

### triggeringPolicy

- SizeBasedTriggeringPolicy

  查看当前活动文件的大小，如果超出指定大小会告知

  - maxFileSize：活动文件的大小，默认值为 10MB。

  ```xml
  <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
      <file>test.log</file>

      <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
          <maxFileSize>5MB</maxFileSize>
      </triggeringPolicy>
  </appender>
  ```

### pattern 转换符说明

| 转换符                                     | 作用                                                           |
| ------------------------------------------ | -------------------------------------------------------------- |
| c{length} / lo{length} / logger{length}    | 输出日志的 logger 名称，                                       |
| C{length} / class{length}                  | 与 logger 作用相同，尽量避免使用                               |
| contextName / cn                           | 输出上下文名称                                                 |
| d{param} / date{param}                     | 日志时间日期，param 为正常的时间格式 `yyyy-MM-dd HH:mm:ss.SSS` |
| F / file                                   | 输出执行记录请求的 java 源文件名，尽量避免使用                 |
| caller{depth, evaluator-1, ...evaluator-n} | 输出生成日志的调用者的位置信息，整数选项表示输出深度。         |
| L / line                                   | 输出执行请求的行号，尽量避免使用                               |
| m / msg / message                          | 输出应用程序提供的信息                                         |
| M / method                                 | 输出执行日志请求的方法名，尽量避免使用。                       |
| n                                          | 换行符                                                         |
| p / le / level                             | 输出日志级别平台                                               |
| r / relative                               | 输出从程序启动到创建日志记录的时间，单位是毫秒                 |
| t / thread                                 | 输出产生日志的线程名。                                         |
| replace(p){r, t}                           | p 为日志内容，r 是正则表达式，将 p 中符合 r 的内容替换为 t 。  |

**格式修饰符，与转换符共同使用：可选的格式修饰符位于 “%” 和转换符之间。**

### filter

    过滤器，执行一个过滤器会有返回枚举值

DENY：日志将立即被抛弃不再经过其他过滤器；

NEUTRAL：有序列表里的下个过滤器接着处理日志；

ACCEPT：日志将被立即处理，不再经过过滤器;

- LevelFilter

  级别过滤器，根据日志界别进行过滤。如果日志等级等于配置级别，过滤器会根据 `onMatch` 和 `onMismatch` 接收或拒绝日志。

  - level：设置过滤级别。
  - onMatch：用于配置符合条件的操作。
  - onMismatch：用于配置不符合过滤条件的操作。

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <filter class="ch.qos.logback.classic.filter.LevelFilter">
      <level>INFO</level>
      <onMatch>ACCEPT</onMatch>
      <onMismatch>DENY</onMismatch>
    </filter>
    <encoder>
      <pattern>
        %-4relative [%thread] %-5level %logger{30} - %msg%n
      </pattern>
    </encoder>
</appender>
```

- ThresholdFilter

  临界值过滤器，过滤掉低于指定临界值得日志。当日志级别等于或高于临界时，过滤器返回 `NEUTRAL`；当日志级别低于临界时，日志会被拒绝。

```xml
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <!-- 过滤掉 TRACE 和 DEBUG 级别的日志-->
    <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
      <level>INFO</level>
    </filter>
    <encoder>
      <pattern>
        %-4relative [%thread] %-5level %logger{30} - %msg%n
      </pattern>
    </encoder>
</appender>
```

## 常用 `logback` 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration debug="false">
    <!-- 上下文名称 -->
    <property name="log.context.name" value="MyApp" />
    <!-- log编码 -->
    <property name="log.charset" value="UTF-8" />
    <!-- log文件最大历史 -->
    <property name="log.history.max" value="30" />
    <!-- log文件输出路径, 相对路径LOG在Tomcat 8.5\bin\LOG下, 绝对路径/LOG在D:\LOG下 -->
    <!-- 推荐绝对路径 -->
    <property name="log.path" value="LOG" />

    <!-- Log4j: [S][%d{yyyyMMdd HH:mm:ss}][%-5p][%C:%L] - %m%n -->
    <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
    <property name="log.pattern" value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n" />
    <property name="log.pattern.short" value="%date{yyyyMMdd HH:mm:ss.SSS}-%msg%n" />


    <!-- 设置上下文名称 -->
    <contextName>${log.context.name}</contextName>

    <!-- 输出到控制台 -->
    <!-- appender用于输出log日志, name是appender的唯一标识, class指定实现类 -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <!-- encoder是编码器, charset指定编码格式 -->
        <encoder charset="${log.charset}">
            <!-- 输出日志的格式, 在上面有提到 -->
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>
    <appender name="STDOUT_SHORT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder charset="${log.charset}">
            <pattern>${log.pattern.short}</pattern>
        </encoder>
    </appender>

    <!-- 输出到文件 -->
    <!-- ERROR级别日志 -->
    <!-- 滚动记录文件，先将日志记录到指定文件，当符合某个条件时，将日志记录到其他文件 RollingFileAppender-->
    <appender name="FILE_ERROR" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录ERROR级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <!-- 匹配则处理这个日志, 不经过其他过滤器 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配则抛弃这个日志 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log.path}/error/log.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${log.history.max}</maxHistory>
        </rollingPolicy>
        <encoder charset="${log.charset}">
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!-- WARN级别日志 appender -->
    <appender name="FILE_WARN" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录WARN级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>WARN</level>
            <!-- 匹配则处理这个日志, 不经过其他过滤器 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配则抛弃这个日志 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log.path}/warn/log.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${log.history.max}</maxHistory>
        </rollingPolicy>
        <encoder charset="${log.charset}">
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!-- INFO级别日志 appender -->
    <appender name="FILE_INFO" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录INFO级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <!-- 匹配则处理这个日志, 不经过其他过滤器 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配则抛弃这个日志 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log.path}/info/log.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${log.history.max}</maxHistory>
        </rollingPolicy>
        <encoder charset="${log.charset}">
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!-- DEBUG级别日志 appender -->
    <appender name="FILE_DEBUG" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录DEBUG级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <!-- 匹配则处理这个日志, 不经过其他过滤器 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配则抛弃这个日志 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log.path}/debug/log.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${log.history.max}</maxHistory>
        </rollingPolicy>
        <encoder charset="${log.charset}">
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!-- TRACE级别日志 appender -->
    <appender name="FILE_TRACE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- 过滤器，只记录TRACE级别的日志 -->
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>TRACE</level>
            <!-- 匹配则处理这个日志, 不经过其他过滤器 -->
            <onMatch>ACCEPT</onMatch>
            <!-- 不匹配则抛弃这个日志 -->
            <onMismatch>DENY</onMismatch>
        </filter>
        <!-- 最常用的滚动策略，它根据时间来制定滚动策略.既负责滚动也负责出发滚动 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志输出位置  可相对、和绝对路径 -->
            <fileNamePattern>${log.path}/trace/log.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- 可选节点，控制保留的归档文件的最大数量，超出数量就删除旧文件假设设置每个月滚动，且<maxHistory>是6，
            则只保存最近6个月的文件，删除之前的旧文件。注意，删除旧文件是，那些为了归档而创建的目录也会被删除-->
            <maxHistory>${log.history.max}</maxHistory>
        </rollingPolicy>
        <encoder charset="${log.charset}">
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>


    <!-- 项目日志级别 -->
    <logger name="com.apache.ibatis" level="TRACE"/>
    <logger name="java.sql.Connection" level="DEBUG"/>
    <logger name="java.sql.Statement" level="DEBUG"/>
    <logger name="java.sql.PreparedStatement" level="DEBUG"/>
    <logger name="com.ahao.project" level="INFO"/>

    <!-- root级别 INFO -->
    <root level="INFO">
        <!-- 控制台输出 -->
        <appender-ref ref="STDOUT" />
        <!-- 文件输出 -->
        <appender-ref ref="FILE_ERROR" />
        <appender-ref ref="FILE_INFO" />
        <appender-ref ref="FILE_WARN" />
        <appender-ref ref="FILE_DEBUG" />
        <appender-ref ref="FILE_TRACE" />
    </root>
</configuration>
```

## 简单的 `logback` 配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration  scan="true" scanPeriod="60 seconds" debug="false">

    <property name="LOG_PATH" value="log"/>
    <property name="APP_NAME" value="take-out"/>
    <property name="LOG_CONSOLE_PATTERN" value="%boldBlue(%-5level) | %date{yyyy-MM-dd HH:mm:ss} | %boldYellow(%thread) | %boldGreen(%logger) | %msg%n"/>
    <property name="LOG_PATTERN" value="%-5level | %date{yyyy-MM-dd HH:mm:ss} | %thread | %logger | %msg%n"/>
    <property name="MAX_HISTORY" value="30"/>
    <property name="MAX_FILE_SIZE" value="5MB"/>

    <contextName>${APP_NAME}</contextName>

    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${LOG_CONSOLE_PATTERN}</pattern>
        </encoder>
    </appender>

    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/${APP_NAME}.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>${MAX_HISTORY}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${LOG_PATTERN}</pattern>
        </encoder>
        <triggeringPolicy class="ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy">
            <maxFileSize>${MAX_FILE_SIZE}</maxFileSize>
        </triggeringPolicy>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```
