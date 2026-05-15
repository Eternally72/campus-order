# 校园交易服务 Campus Order

面向校园场景的二手交易微服务平台，覆盖用户认证、商品发布与检索、订单交易、消息通知、权限校验和 AI 辅助能力。项目基于 Spring Boot 与 Spring Cloud Alibaba 构建，围绕服务拆分、网关统一入口、服务注册发现、接口限流、缓存优化、消息异步化和分布式事务一致性进行设计。

## 技术栈

| 类别 | 技术 |
| --- | --- |
| 基础框架 | Spring Boot 3.5.14, Spring Cloud 2025.0.0, Spring Cloud Alibaba 2025.0.0.0 |
| 服务治理 | Nacos Discovery, Nacos Config, Spring Cloud LoadBalancer |
| 网关与限流 | Spring Cloud Gateway, Sentinel Gateway |
| 服务调用 | OpenFeign |
| 认证鉴权 | Sa-Token, Sa-Token Redis |
| 数据访问 | MySQL, MyBatis, PageHelper |
| 缓存 | Redis |
| 消息队列 | RabbitMQ |
| 分布式事务 | Seata AT |
| AI 能力 | LangChain4j, Zhipu AI, MCP, Milvus |
| 构建工具 | JDK 21, Maven 3.9.15 |

## 项目结构

```text
campus-order
├── gateway   # 统一网关，路由转发、登录校验、Sentinel 限流
├── auth      # 认证服务，登录、注册、Token 管理、权限角色接口
├── user      # 用户服务，用户资料、密码校验、收藏管理
├── goods     # 商品服务，商品发布、分类、上下架、库存与收藏计数
├── order     # 订单服务，创建、取消、支付、发货、确认收货
├── notify    # 通知服务，消费订单消息并维护通知记录
├── ai        # AI 服务，对话、流式响应、RAG/MCP 能力集成
├── common    # 公共 DTO、VO、实体、统一响应、异常、工具和 Feign 契约
└── document  # 数据库脚本与事务脚本
```

## 服务端口

| 模块 | 服务名 | 默认端口 | 说明 |
| --- | --- | --- | --- |
| gateway | gateway-service | 8080 | API 统一入口 |
| auth | auth-service | 8081 | 登录、注册、Token |
| user | user-service | 8082 | 用户与收藏 |
| goods | goods-service | 8083 | 商品、分类、库存 |
| order | order-service | 8084 | 订单交易 |
| notify | notify-service | 8085 | 消息通知 |
| ai | ai-service | 8086 | AI 能力 |

## 核心能力

- 微服务拆分：按认证、用户、商品、订单、通知、AI、网关和公共契约拆分模块。
- 统一网关：通过 Gateway 暴露 `/api/**` 路由，集中处理认证透传和 Sentinel 网关限流。
- 服务注册发现：通过 Nacos 进行服务发现与配置管理。
- 远程调用：Auth 调用 User 完成登录注册，Order 调用 Goods 完成商品校验与库存扣减，User 调用 Goods 同步收藏计数。
- 分布式事务：订单创建、取消等跨订单与库存场景通过 Seata AT 保证一致性。
- 异步通知：Order 通过 RabbitMQ 发布订单事件，Notify 消费事件生成通知记录。
- 缓存与会话：Redis 支撑 Sa-Token 会话、验证码和公共缓存能力。
- AI 集成：AI 模块集成 LangChain4j、智谱模型、MCP Web Search 和 Milvus 向量检索能力。

## 网关路由

| 外部路径 | 目标服务 |
| --- | --- |
| `/api/auth/**` | auth-service |
| `/api/user/**` | user-service |
| `/api/goods/**` | goods-service |
| `/api/order/**` | order-service |
| `/api/notify/**` | notify-service |
| `/api/ai/**` | ai-service |

## 环境要求

- JDK 21
- Maven 3.9.15+
- MySQL 8+
- Redis 6+
- RabbitMQ 3+
- Nacos 2+
- Seata Server 2.x
- Sentinel Dashboard
- 可选：Milvus、智谱 AI API Key

## 快速开始

1. 初始化数据库：

```bash
mysql -u root -p < document/sql/schema.sql
mysql -u root -p campus_order < document/sql/undo_log.sql
```

2. 启动基础设施：

```text
MySQL
Redis
RabbitMQ
Nacos
Seata Server
Sentinel Dashboard
```

3. 按需配置环境变量：

| 环境变量 | 默认值 | 说明 |
| --- | --- | --- |
| `NACOS_ADDR` | `localhost:8848` | Nacos 地址 |
| `MYSQL_HOST` | `localhost` | MySQL 主机 |
| `MYSQL_PORT` | `3306` | MySQL 端口 |
| `MYSQL_DATABASE` | `campus_order` | 数据库名 |
| `MYSQL_USERNAME` | `root` | 数据库用户名 |
| `MYSQL_PASSWORD` | `123456` | 数据库密码 |
| `REDIS_HOST` | `localhost` | Redis 主机 |
| `REDIS_PORT` | `6379` | Redis 端口 |
| `RABBITMQ_HOST` | `localhost` | RabbitMQ 主机 |
| `RABBITMQ_PORT` | `5672` | RabbitMQ 端口 |
| `SENTINEL_DASHBOARD` | `localhost:8858` | Sentinel 控制台 |
| `ZHIPU_API_KEY` | `your-api-key` | 智谱 AI Key |

4. 编译项目：

```bash
mvn clean package -DskipTests
```

5. 启动服务：

```bash
java -jar gateway/target/gateway-1.0-SNAPSHOT.jar
java -jar auth/target/auth-1.0-SNAPSHOT.jar
java -jar user/target/user-1.0-SNAPSHOT.jar
java -jar goods/target/goods-1.0-SNAPSHOT.jar
java -jar order/target/order-1.0-SNAPSHOT.jar
java -jar notify/target/notify-1.0-SNAPSHOT.jar
java -jar ai/target/ai-1.0-SNAPSHOT.jar
```

## 关键业务流程

### 订单创建

1. 客户端通过网关提交订单请求。
2. Order Service 开启 Seata 全局事务。
3. Order Service 通过 Feign 查询 Goods Service 商品详情。
4. Goods Service 校验商品状态并扣减库存。
5. Order Service 创建订单记录。
6. Order Service 发布订单创建消息到 RabbitMQ。
7. Notify Service 消费消息并生成通知。

### 登录认证

1. Auth Service 生成验证码并写入 Redis。
2. 用户提交登录信息。
3. Auth Service 通过 Feign 调用 User Service 校验用户名和密码。
4. 校验成功后通过 Sa-Token 签发 Token。
5. Gateway 校验 Token 并向下游服务透传 `X-User-Id`。

### 收藏同步

1. User Service 创建或删除收藏记录。
2. User Service 通过 Feign 调用 Goods Service。
3. Goods Service 同步增加或减少商品收藏数。

## 数据库

项目默认使用 `campus_order` 数据库，核心表包括：

- `t_user`：用户表
- `t_category`：商品分类表
- `t_goods`：商品表
- `t_order`：订单表
- `t_favorite`：收藏表
- `t_notify_record`：通知记录表
- `undo_log`：Seata AT 事务回滚日志表

脚本位置：

```text
document/sql/schema.sql
document/sql/undo_log.sql
```

## 构建验证

```bash
mvn -DskipTests compile
mvn -DskipTests package
```

当前项目已适配 JDK 21，并通过 Maven 多模块编译与打包验证。

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
