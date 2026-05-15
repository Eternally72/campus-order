# 校园二手交易平台项目框架

## 项目概述
本项目是一个基于 Spring Cloud Alibaba 的校园二手物品交易平台微服务系统，采用高内聚、低耦合的架构设计。

## 技术栈
- **核心框架**: Spring Boot 3.5.x + Spring Cloud 2025.x + Spring Cloud Alibaba 2025.x
- **服务注册与发现**: Nacos
- **网关**: Spring Cloud Gateway + Sentinel
- **远程调用**: OpenFeign + LoadBalancer
- **分布式事务**: Seata AT模式
- **认证鉴权**: Sa-Token
- **消息队列**: RabbitMQ
- **数据库**: MySQL + MyBatis + PageHelper
- **缓存**: Redis
- **AI框架**: LangChain4j

## 项目统计
- Java文件: 82个
- XML配置文件: 22个（包含pom.xml和Mapper.xml）

## 模块说明

### 1. gateway (端口: 8080)
- 统一入口网关
- 集成 Sentinel 流量控制和熔断降级
- Sa-Token 全局认证过滤器
- 路由转发配置
- 限流异常处理

### 2. auth (端口: 8081)
- 用户登录/注册
- Sa-Token 认证鉴权
- 权限校验接口（StpInterfaceImpl）
- Feign调用用户服务

### 3. user (端口: 8082)
- 用户信息管理（CRUD）
- 用户收藏管理
- 密码验证接口
- 提供用户相关 Feign 接口

### 4. goods (端口: 8083)
- 商品发布/管理
- 商品分类管理（树形结构）
- 库存管理（扣减/恢复）- 用于分布式事务
- 商品搜索（关键词、分类、价格等）
- 提供商品相关 Feign 接口

### 5. order (端口: 8084)
- 订单创建（@GlobalTransactional分布式事务）
- 订单取消（恢复库存）
- 订单支付/发货/确认收货
- 发送订单消息到 RabbitMQ
- 买家/卖家订单查询

### 6. notify (端口: 8085)
- RabbitMQ 消息监听
- 订单通知处理（创建、支付、取消、完成）
- 死信队列处理
- 通知记录管理（已读/未读）

### 7. ai (端口: 8086)
- AI 对话（同步/流式）
- 商品描述智能摘要
- RAG 检索增强问答
- TODO: 由用户自行实现 LangChain4j 逻辑

### 8. common
- 公共实体类（BaseEntity）
- 统一响应封装（Result、ResultCodeEnum）
- 全局异常处理（GlobalExceptionHandler）
- 工具类（IdUtil、PasswordUtil、RedisUtil）
- 公共 Feign 接口
- 公共常量（RabbitMQConstant、CommonConstant）
- 分页DTO（PageQueryDTO、PageVO）
- Redis配置

## 数据库表
- t_user: 用户表
- t_goods: 商品表
- t_category: 商品分类表
- t_order: 订单表
- t_favorite: 用户收藏表
- t_notify_record: 通知记录表

## Mapper文件
- UserMapper.xml
- FavoriteMapper.xml
- GoodsMapper.xml
- CategoryMapper.xml
- OrderMapper.xml
- NotifyRecordMapper.xml

## TODO 清单
以下是需要用户自行补充的内容：

### 配置相关
- [ ] 各模块的数据库连接配置（用户名、密码）
- [ ] Redis 密码配置
- [ ] RabbitMQ 连接配置（用户名、密码）
- [ ] Nacos 命名空间配置
- [ ] Seata 配置（需要先部署 Seata Server）
- [ ] Sentinel 控制台地址

### 业务相关
- [ ] 验证码生成与校验
- [ ] 用户权限数据查询（StpInterfaceImpl中从数据库查询）
- [ ] 完善 AuthServiceImpl 中的用户验证调用
- [ ] 商品图片上传（OSS/本地存储）
- [ ] 订单支付集成（支付宝/微信）

### AI 模块
- [ ] LangChain4j 模型配置（API Key等）
- [ ] AI 对话实现
- [ ] RAG 向量存储配置
- [ ] Tools 工具开发
- [ ] MCP 集成

## 启动顺序
1. 启动 Nacos Server
2. 启动 MySQL、Redis、RabbitMQ
3. 启动 Seata Server（可选）
4. 启动 gateway (8080)
5. 启动其他微服务：
   - auth (8081)
   - user (8082)
   - goods (8083)
   - order (8084)
   - notify (8085)
   - ai (8086)

## API 路由
| 路径 | 服务 | 说明 |
|------|------|------|
| `/api/auth/**` | auth-service | 认证相关 |
| `/api/user/**` | user-service | 用户相关 |
| `/api/goods/**` | goods-service | 商品相关 |
| `/api/order/**` | order-service | 订单相关 |
| `/api/notify/**` | notify-service | 通知相关 |
| `/api/ai/**` | ai-service | AI相关 |

## 核心功能流程

### 订单创建流程（分布式事务）
1. 用户提交订单 -> Order Service
2. @GlobalTransactional 开启分布式事务
3. 调用 Goods Service 扣减库存（Feign）
4. 创建订单记录
5. 发送订单消息到 RabbitMQ
6. 事务提交/回滚

### 消息通知流程
1. Order Service 发送消息到 RabbitMQ
2. Notify Service 监听队列
3. 处理消息，创建通知记录
4. 用户查询通知列表
