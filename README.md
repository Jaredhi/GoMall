# 项目简介

该项目是一套电商项目，包括前台商城系统以及后台管理系统，基于 SpringCloud + SpringCloudAlibaba + MyBatis-Plus实现，采用 Docker 容器化部署。前台商城系统包括：用户登录、注册、商品搜索、商品详情、购物车、下订单流程、秒杀活动等模块。后台管理系统包括：系统管理、商品系统、优惠营销、库存系统、订单系统、用户系统、内容管理等七大模块。

➜[后台管理前端代码](https://gitee.com/jinterest/go-mall-front)

## 组织结构

```
GoMall-Java
├── gomall-auth-server -- 认证中心（社交登录、OAuth2.0、单点登录）
├── gomall-cart -- 购物车服务
├── gomall-common -- 工具类及通用代码
├── gomall-coupon -- 优惠卷服务
├── gomall-gateway -- 统一配置网关
├── gomall-member -- 会员服务
├── gomall-order -- 订单服务
├── gomall-product -- 商品服务
├── gomall-search -- 检索服务
├── gomall-seckill -- 秒杀服务
├── gomall-third-party -- 第三方服务
├── gomall-ware -- 仓储服务
├── renren-fast -- 人人开源后台管理
└── renren-generator -- 人人开源项目的代码生成器
```

## 技术选型

### 后端技术

| 技术               | 说明         | 官网                                            |
| :----------------- | :----------- | :---------------------------------------------- |
| SpringBoot         | 容器+MVC框架 | https://spring.io/projects/spring-boot          |
| SpringCloud        | 微服务架构   | https://spring.io/projects/spring-cloud         |
| SpringCloudAlibaba | 一系列组件   | https://spring.io/projects/spring-cloud-alibaba |
| MyBatis-Plus       | ORM框架      | https://mp.baomidou.com                         |
| renren             | 人人开源     | https://gitee.com/renrenio                      |
| Elasticsearch      | 搜索引擎     | https://github.com/elastic/elasticsearch        |
| RabbitMQ           | 消息队列     | https://www.rabbitmq.com                        |
| Springsession      | 分布式缓存   | https://projects.spring.io/spring-session       |
| Redisson           | 分布式锁     | https://github.com/redisson/redisson            |
| Docker             | 应用容器引擎 | https://www.docker.com                          |
| OSS                | 对象云存储   | https://github.com/aliyun/aliyun-oss-java-sdk   |
|                    |              |                                                 |
|                    |              |                                                 |



### 前端技术

| 技术       | 说明       | 官网                      |
| :--------- | :--------- | :------------------------ |
| Vue        | 前端框架   | https://vuejs.org         |
| Element-UI | 前端UI框架 | https://element.eleme.io  |
| thymeleaf  | 模板引擎   | https://www.thymeleaf.org |
| node.js    | 服务端的js | https://nodejs.org/en     |





## 环境搭建

### 开发工具

| 工具          | 说明                | 官网                                                         |
| :------------ | ------------------- | ------------------------------------------------------------ |
| IDEA          | Java开发            | https://www.jetbrains.com/idea/download                      |
| VMware Fusion | 构建虚拟机          | https://www.vmware.com/cn/products/fusion/fusion-evaluation.html |
| RDM           | redis客户端连接工具 | https://rdm.dev                                              |
| SwitchHosts   | 本地host管理        | https://oldj.github.io/SwitchHosts                           |
| X-shell       | Linux远程连接工具   | http://www.netsarang.com/download/software.html              |
| Navicat       | 数据库连接工具      | http://www.formysql.com/xiazai.html                          |
| PowerDesigner | 数据库设计工具      | http://powerdesigner.de                                      |
| Postman       | API接口调试工具     | https://www.postman.com                                      |
| Jmeter        | 性能压测工具        | https://jmeter.apache.org                                    |
| Typora        | Markdown编辑器      | https://typora.io                                            |



### 开发环境

| 工具          | 版本号     | 官网下载地址                                                 |
| ------------- | ---------- | ------------------------------------------------------------ |
| JDK           | 1.8        | https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html |
| Mysql         | 5.7        | https://www.mysql.com                                        |
| Redis         | latest     | https://redis.io/download                                    |
| Elasticsearch | 7.4.2      | https://www.elastic.co/downloads                             |
| Kibana        | 7.4.2      | https://www.elastic.co/cn/kibana                             |
| RabbitMQ      | management | http://www.rabbitmq.com/download.html                        |
| Nginx         | 1.10       | http://nginx.org/en/download.html                            |
|               |            |                                                              |


注意：以上的除了jdk都是采用docker方式进行安装，详细安装步骤请参考《安装文档》!!!



