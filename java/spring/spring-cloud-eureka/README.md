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

页面被分为了五大块:

- System Status，主要展示系统状态，比如启动时间等
- DS Replicas，该服务从哪里同步数据
- Instances currently registered with Eureka，注册在 Eureka 的实例列表
- General Info，系统运行环境，比如内存、cpu 等
- Instance Info，本服务的基础信息，比如 ip 地址，状态等

## Eureka 工作流程

1. Eureka Server 启动成功，等待服务端注册。如果是集群启动，集群之间定时通过 Replicate 同步注册表，每个 Eureka Server 都存在独立完成的服务注册信息；
2. Eureka Client 启动时根据配置的 Eureka Server 地址去注册中心注册服务；
3. Eureka Client 会每 30s 向 Eureka Server 发送一次心跳请求，证明客户端服务正常，该值可通过 `eureka.instance.lease-renewal-interval-in-seconds` 设置；
4. Eureka Server 90s 内没有收到 Eureka Client 的心跳，注册中心则会注销该实例，该值可通过 `ureka.instance.lease-expiration-duration-in-seconds` 设置；
5. Eureka Server 在运行期间会统计心跳失败比例在 15 分钟之内是低于 85%，如果低于 85%，Eureka Server 即会进入自我保护机制，可通过 ` eureka.server.enable-self-preservation=true` 设置保护机制开启，保护机制下会出现如下情况：
  - Eureka 不在从注册表中移除因为长时间没收到心跳而应该过期的服务；
  - Eureka 仍然能接受新的服务注册和查询请求，但不会同步到其他节点上；
  - 网络稳定时，当前实例新的注册信息会被同步到其他节点上，并退出自我保护机制；
6. Eureka Client 定时全量或增量从注册中心获取服务注册表，并缓存到本地；
7. 服务调用时，Eureka Client 先从本地缓存查找，如果找不到，先从注册中心刷新注册表，在同步到本地缓存；
8. Eureka Client 程序关闭时，向 Eureka Server 发送取消请求，Eureka Server 从注册表中将其删除；

## Eureka Server 数据存储

Eureka Server 的数据存储分了两层：数据存储层 和 缓存层。数据存储层记录注册到 Eureka Server 上的服务信息，缓存层是经过包装后的数据，可以直接在 Eureka Client 调用时返回。

Eureka Server 的数据存储是双层的 ConcurrentHashMap

```java
  private final ConcurrentHashMap<String, Map<String, Lease<InstanceInfo>>> registry= new ConcurrentHashMap<String, Map<String, Lease<InstanceIn fo>>>();
```

- 第一层的 ConcurrentHashMap 的 `key = spring.application.name` ，也就是客户端实例注册的应用名; value 为嵌套的 ConcurrentHashMap；
- 第二层嵌套的 ConcurrentHashMap 的 `key = instanceId`，也就是服务的唯一实例 ID，value 为 Lease 对象，Lease 对象存储着这个实例的所有注册信息，包括 ip 、端口、属性等；

## Eureka Server 缓存机制

Eureka Server 采用了 双层缓存结构，将 Eureka Client 所需要的注册信息，直接存储在缓存结构中

##### 第一层缓存 readOnlyCacheMap

是一个 CurrentHashMap 只读缓存，主要是为了提供客户端获取注册信息时使用，缓存更新依赖于定时器更新，通过和 readWriteCacheMap 的值对比，如果值不一致，则以 readWriteCacheMap 的数据为准.

##### 第二层缓存 readWriteCacheMap

本质是一个 Guava 缓存，其数据主要同步于存储层，获取缓存中有无该数据，如果没有，则通过 CacheLoader 的 load 方法加载。
readWriteCacheMap 缓存过期时间，默认为 180s，当服务下线、过期、注册、状态变更，都会来清除此缓存中的数据。

Eureka Client 获取数据时，先从一级缓存获取；如果不存在，再从二级缓存获取；如果二级缓存也不存在，则现将存储层数据同步到缓存中，再从缓存中获取。

## Eureka 常用配置

##### Eureka Server

```properties
# 服务端开启自我保护模式
eureka.server.enable-self-preservation=true
# 扫描失效服务的间隔时间(单位毫秒，默认是60*1000)即60秒
eureka.server.eviction-interval-timer-in-ms= 60000
# 间隔多长时间，清除过期的 delta 数据
eureka.server.delta-retention-timer-interval-in-ms=0
# 请求频率限制器
eureka.server.rate-limiter-burst-size=10
# 是否开启请求频率限制器
eureka.server.rate-limiter-enabled=false
# 请求频率的平均值
eureka.server.rate-limiter-full-fetch-average-rate=100
# 是否对标准的client进行频率请求限制。如果是false，则只对非标准client进行限制
eureka.server.rate-limiter-throttle-standard-clients=false
# 注册服务、拉去服务列表数据的请求频率的平均值
eureka.server.rate-limiter-registry-fetch-average-rate=500
# 设置信任的client list
eureka.server.rate-limiter-privileged-clients=
# 在设置的时间范围类，期望与client续约的百分比
eureka.server.renewal-percent-threshold=0.85
# 多长时间更新续约的阈值
eureka.server.renewal-threshold-update-interval-ms=0
# 对于缓存的注册数据，多长时间过期
eureka.server.response-cache-auto-expiration-in-seconds=180
# 多长时间更新一次缓存中的服务注册数据
eureka.server.response-cache-update-interval-ms=0
# 缓存增量数据的时间，以便在检索的时候不丢失信息
eureka.server.retention-time-in-m-s-in-delta-queue=0
# 当时间戳不一致的时候，是否进行同步
eureka.server.sync-when-timestamp-differs=true
# 是否采用只读缓存策略，只读策略对于缓存的数据不会过期
eureka.server.use-read-only-response-cache=true

################server node 与 node 之间关联的配置#####################33
# 发送复制数据是否在request中，总是压缩
eureka.server.enable-replicated-request-compression=false
# 指示群集节点之间的复制是否应批处理以提高网络效率
eureka.server.batch-replication=false
# 允许备份到备份池的最大复制事件数量。而这个备份池负责除状态更新的其他事件。可以根据内存大小，超时和复制流量，来设置此值得大小
eureka.server.max-elements-in-peer-replication-pool=10000
# 允许备份到状态备份池的最大复制事件数量
eureka.server.max-elements-in-status-replication-pool=10000
# 多个服务中心相互同步信息线程的最大空闲时间
eureka.server.max-idle-thread-age-in-minutes-for-peer-replication=15
# 状态同步线程的最大空闲时间
eureka.server.max-idle-thread-in-minutes-age-for-status-replication=15
# 服务注册中心各个instance相互复制数据的最大线程数量
eureka.server.max-threads-for-peer-replication=20
# 服务注册中心各个instance相互复制状态数据的最大线程数量
eureka.server.max-threads-for-status-replication=1
# instance之间复制数据的通信时长
eureka.server.max-time-for-replication=30000
# 正常的对等服务instance最小数量。-1表示服务中心为单节点
eureka.server.min-available-instances-for-peer-replication=-1
# instance之间相互复制开启的最小线程数量
eureka.server.min-threads-for-peer-replication=5
# instance之间用于状态复制，开启的最小线程数量
eureka.server.min-threads-for-status-replication=1
# instance之间复制数据时可以重试的次数
eureka.server.number-of-replication-retries=5
# eureka节点间间隔多长时间更新一次数据。默认10分钟
eureka.server.peer-eureka-nodes-update-interval-ms=600000
# eureka服务状态的相互更新的时间间隔
eureka.server.peer-eureka-status-refresh-time-interval-ms=0
# eureka对等节点间连接超时时间
eureka.server.peer-node-connect-timeout-ms=200
# eureka对等节点连接后的空闲时间
eureka.server.peer-node-connection-idle-timeout-seconds=30
# 节点间的读数据连接超时时间
eureka.server.peer-node-read-timeout-ms=200
# eureka server 节点间连接的总共最大数量
eureka.server.peer-node-total-connections=1000
# eureka server 节点间连接的单机最大数量
eureka.server.peer-node-total-connections-per-host=10
# 在服务节点启动时，eureka尝试获取注册信息的次数
eureka.server.registry-sync-retries=
# 在服务节点启动时，eureka多次尝试获取注册信息的间隔时间
eureka.server.registry-sync-retry-wait-ms=
#当eureka server启动的时候，不能从对等节点获取instance注册信息的情况，应等待多长时间
eureka.server.wait-time-in-ms-when-sync-empty=0
```

##### Eureka Client

```properties
# 该客户端是否可用
eureka.client.enabled=true
# 实例是否在eureka服务器上注册自己的信息以供其他服务发现，默认为true
eureka.client.register-with-eureka=false
# 此客户端是否获取eureka服务器注册表上的注册信息，默认为true
eureka.client.fetch-registry=false
# 是否过滤掉，非UP的实例。默认为true
eureka.client.filter-only-up-instances=true
#与Eureka注册服务中心的通信zone和url地址
eureka.client.serviceUrl.defaultZone=http://${eureka.instance.hostname}:${server.port}/eureka/
#client连接Eureka
# 从eureka服务器注册表中获取注册信息的时间间隔(s)，默认为30秒
eureka.client.registry-fetch-interval-seconds=30
# 获取实例所在的地区。默认为us-east-1
eureka.client.region=us-east-1
# 实例是否使用同一zone里的eureka服务器，默认为true，理想状态下，eureka客户端与服务端是在同一zone下
eureka.client.prefer-same-zone-eureka=true
# 获取实例所在的地区下可用性的区域列表，用逗号隔开。(AWS)
eureka.client.availability-zones.china=defaultZone,defaultZone1,defaultZone2
# eureka服务注册表信息里的以逗号隔开的地区名单，如果不这样返回这些地区名单，则客户端启动将会出错。默认为null
eureka.client.fetch-remote-regions-registry=
# 服务器是否能够重定向客户端请求到备份服务器。 如果设置为false，服务器将直接处理请求，如果设置为true，它可能发送HTTP重定向到客户端。 默认为false
eureka.client.allow-redirects=false
# 客户端数据接收
eureka.client.client-data-accept=
# 增量信息是否可以提供给客户端看，默认为false
eureka.client.disable-delta=false
# eureka服务器序列化/反序列化的信息中获取“_”符号的的替换字符串。默认为“__“
eureka.client.escape-char-replacement=__
# eureka服务器序列化/反序列化的信息中获取“$”符号的替换字符串。默认为“_-”
eureka.client.dollar-replacement="_-"
# 当服务端支持压缩的情况下，是否支持从服务端获取的信息进行压缩。默认为true
eureka.client.g-zip-content=true
# 是否记录eureka服务器和客户端之间在注册表的信息方面的差异，默认为false
eureka.client.log-delta-diff=false
# 如果设置为true,客户端的状态更新将会点播更新到远程服务器上，默认为true
eureka.client.on-demand-update-status-change=true
# 此客户端只对一个单一的VIP注册表的信息感兴趣。默认为null
eureka.client.registry-refresh-single-vip-address=
# client是否在初始化阶段强行注册到服务中心，默认为false
eureka.client.should-enforce-registration-at-init=false
# client在shutdown的时候是否显示的注销服务从服务中心，默认为true
eureka.client.should-unregister-on-shutdown=true
```


##### Eureka Instance

```properties
# 服务注册中心实例的主机名
eureka.instance.hostname=localhost
# 注册在Eureka服务中的应用组名
eureka.instance.app-group-name=
# 注册在的Eureka服务中的应用名称
eureka.instance.appname=
# 该实例注册到服务中心的唯一ID
eureka.instance.instance-id=
# 该实例的IP地址
eureka.instance.ip-address=
# 该实例，相较于hostname是否优先使用IP
eureka.instance.prefer-ip-address=false
```