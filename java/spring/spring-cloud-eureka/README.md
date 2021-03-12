## Eureka 介绍

Eureka 是 Netflix 公司开源的产品，它是一种基于 REST(Representational State Transfer)的服务，主要用于 AWS 云。Eureka 提供了完整的 Service Registry 和 Service Discovery 实现，也是 Spring Cloud 体系中最重要最 核心的组件之一。

Eureka 由两个组件组成: Eureka 服务端和 Eureka 客户端。Eureka 服务端就是注册中心。Eureka 客户端是一个 java 客户端，用来简化与服务端的交互、作为轮询负载均衡器，并提供服务的故障切换支持，角色对应关系如下

- Eureka Server: 担任注册中心的角色，提供了服务的注册和发现功能；
- Service Provider: 服务提供者，将自身服务注册到 Eureka Server，同时通过心跳来检查服务的运行状态
- Service Consumer: 服务调用者，从 Eureka 获取注册服务列表，找到对应的服务地址再进行调用

## 搭建 Eureka Server

1. 添加 Eureka 依赖

```xml
<dependencies>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

2. 在启动类中添加 `@EnableEurekaServer`
```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(EurekaServerApplication.class, args);
	}
}
```

3. 设置配置文件
```yaml
server:
	# 设置服务端口号
	port: 8761

########################
# Eureka Server 配置
########################
eureka:
	instance:
		# eureka 服务主机名名称, 用于查找主机地址
		hostname: eureka1
	server:
		# 关闭保护机制
		enable-self-preservation: false
	client:
		#    # 是否向服务中心注册 默认 true
		#    register-with-eureka: false
		#    # 是否从注册中心检索服务 默认 true
		#    fetch-registry: false
		service-url:
			# 注册中心地址
			defaultZone: http://eureka2:8762/eureka/
```

4. 启动 Eureka 服务端，访问地址 `http://localhost:8761`

台页面被分为了五大块:

- System Status，主要展示系统状态，比如启动时间等
- DS Replicas，该服务从哪里同步数据
- Instances currently registered with Eureka，注册在 Eureka 的实例列表
- General Info，系统运行环境，比如内存、cpu 等
- Instance Info，本服务的基础信息，比如 ip 地址，状态等

## Eureka 原理

