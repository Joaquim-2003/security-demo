# Security Demo

一个用于重新梳理和实践 **全局异常处理、JWT 认证、Spring Security 与 RBAC 权限控制** 的后端练习项目。

项目重点不在业务 CRUD，而在于把认证、授权和异常处理的完整调用链拆开实现并验证，形成对以下问题的清晰理解：

- JWT 如何生成、解析和校验
- JWT 如何转换为 Spring Security 中的认证身份
- Spring Security 如何保存当前请求的用户信息
- RBAC 如何根据用户加载权限
- `@PreAuthorize` 如何完成方法级权限判断
- 401、403、业务异常和系统异常分别如何处理

详细认证授权流程见：

[SECURITY_AUTH_FLOW.md](./SECURITY_AUTH_FLOW.md)

---

## 1. 技术栈

| 技术 | 当前项目版本 / 用途 |
| --- | --- |
| Java | 17 |
| Spring Boot | 4.0.7 |
| Spring Security | 认证与授权 |
| JJWT | 0.12.6，JWT 生成、解析与验签 |
| MyBatis | 4.0.1 |
| MySQL | 用户、角色与权限数据存储 |
| Jakarta Validation | 参数校验 |
| Lombok | 简化 Java Bean 与构造注入 |
| Maven | 项目构建与依赖管理 |

---

## 2. 当前已实现功能

### 全局异常处理

项目通过统一异常处理机制处理：

- 自定义业务异常 `BizException`
- 参数校验异常
- 方法级权限不足异常
- 未知系统异常

正常响应与异常响应统一使用 `Result<T>` 和 `ResultCode` 表达。

### JWT

当前 JWT 模块已经实现：

- 根据 `userId`、`username` 生成 Token
- 设置签发时间与过期时间
- 使用 HMAC 密钥完成 JWT 签名
- 解析 JWT Claims
- 校验 JWT 签名与有效期
- JWT 正常、篡改和过期场景测试

### Spring Security

当前项目采用无状态认证模式：

- `SessionCreationPolicy.STATELESS`
- 禁用 Form Login
- 禁用 HTTP Basic
- JWT Filter 接入 Spring Security Filter Chain
- 使用 `SecurityContextHolder` 保存当前请求认证信息
- 未认证请求统一返回 HTTP 401
- 保留 Filter Chain 层的 HTTP 403 处理机制

### RBAC

当前项目实现了基础 RBAC 权限模型：

```text
User
  ↓
UserRole
  ↓
Role
  ↓
RolePermission
  ↓
Permission
```

权限加载完成后，将数据库中的 `permission_code` 转换为 Spring Security 可识别的 `GrantedAuthority`。

目前通过：

```java
@PreAuthorize("hasAuthority('user:add')")
```

等方法级权限表达式完成接口授权。

---

## 3. 核心执行流程

```text
HTTP Request
      ↓
读取并校验 JWT
      ↓
获取 userId
      ↓
查询当前用户
      ↓
查询用户 RBAC 权限
      ↓
组装 LoginUser
      ↓
转换 GrantedAuthority
      ↓
创建 Authentication
      ↓
保存到 SecurityContext
      ↓
进入 Controller
      ↓
@PreAuthorize 权限判断
      ↓
 ┌────────────┬────────────┬────────────┐
 │            │            │            │
未认证       无权限       有权限       业务异常
 │            │            │            │
401          403        正常执行      全局异常处理
```

更完整的原理图和项目文件协作关系：

[查看认证授权流程图](./SECURITY_AUTH_FLOW.md)

---

## 4. 项目结构

```text
security-demo
├── sql
│   ├── schema.sql
│   └── data.sql
│
├── src
│   ├── main
│   │   ├── java/com/jing/security
│   │   │   ├── common
│   │   │   │   ├── enums
│   │   │   │   └── result
│   │   │   │
│   │   │   ├── config
│   │   │   │   └── properties
│   │   │   │
│   │   │   ├── controller
│   │   │   ├── exception
│   │   │   ├── handler
│   │   │   ├── mapper
│   │   │   ├── pojo
│   │   │   ├── security
│   │   │   │   ├── filter
│   │   │   │   ├── model
│   │   │   │   └── service
│   │   │   └── util
│   │   │
│   │   └── resources
│   │       ├── mapper
│   │       ├── application.yaml
│   │       └── application-local.yaml
│   │
│   └── test
│
├── pom.xml
├── README.md
└── SECURITY_AUTH_FLOW.md
```

---

## 5. 核心模块职责

| 模块 | 主要职责 |
| --- | --- |
| `SecurityConfig` | 配置 Security Filter Chain、无状态认证、接口访问规则及 401/403 Handler |
| `JwtAuthenticationFilter` | 从请求头读取 JWT，将合法 Token 转换为当前请求认证身份 |
| `JwtUtil` | JWT 生成、解析、签名和有效期校验 |
| `JwtProperties` | 加载 JWT 密钥、过期时间、Header 和 Prefix 配置 |
| `SecurityUserServiceImpl` | 根据用户 ID 查询用户及权限，并构造当前登录用户 |
| `SysUserMapper` | 查询系统用户 |
| `PermissionMapper` | 查询当前用户拥有的权限码 |
| `LoginUser` | 保存当前登录用户的基本信息和 `GrantedAuthority` 集合 |
| `JwtAuthenticationEntryPoint` | 处理未认证场景并返回 401 |
| `JwtAccessDeniedHandler` | 处理 Filter Chain 层面的权限不足并返回 403 |
| `GlobalExceptionHandler` | 统一处理业务、校验、方法级权限及系统异常 |
| `ResponseUtil` | 在 Security Handler 场景中将响应对象序列化为 JSON |
| `Result / ResultCode` | 定义统一响应结构和状态信息 |

---

## 6. 数据库设计

数据库名称：

```text
security-demo
```

项目当前使用 5 张 RBAC 相关表。

### `sys_user`

保存系统用户：

```text
id
username
password
```

### `sys_role`

保存角色：

```text
id
role_code
```

### `sys_user_role`

建立用户与角色的多对多关系：

```text
user_id
role_id
```

### `sys_permission`

保存权限标识：

```text
id
permission_code
```

示例：

```text
user:list
user:add
user:delete
```

### `sys_role_permission`

建立角色与权限的多对多关系：

```text
role_id
permission_id
```

建表和测试数据位于：

```text
sql/schema.sql
sql/data.sql
```

---

## 7. 当前测试数据

初始化数据包含两个用户角色：

```text
admin → ADMIN
user  → USER
```

权限关系：

```text
ADMIN
├── user:list
├── user:add
└── user:delete

USER
└── user:list
```

这些数据仅用于验证 RBAC 权限链路。

---

## 8. 测试接口

当前测试 Controller 基础路径：

```text
/security
```

### 普通认证测试

```http
POST /security/test
POST /security/protected
```

### `user:add` 权限测试

```http
POST /security/add
```

要求：

```text
user:add
```

### `user:list` 权限测试

```http
GET /security/list
```

要求：

```text
user:list
```

访问受保护接口时，需要携带：

```http
Authorization: Bearer <JWT>
```

---

## 9. 本地运行

### 9.1 环境要求

本地需要准备：

- JDK 17
- Maven
- MySQL

### 9.2 创建数据库

创建：

```sql
CREATE DATABASE `security-demo`;
```

然后按顺序执行：

```text
sql/schema.sql
sql/data.sql
```

### 9.3 配置本地环境

项目默认启用：

```yaml
spring:
  profiles:
    active: local
```

请在 `application-local.yaml` 中配置自己的 MySQL 连接信息。

建议不要在公开仓库中提交真实数据库密码或生产环境 JWT 密钥。

示例：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/security-demo
    username: your_username
    password: your_password

jwt:
  secret-key: your-long-secret-key
```

JWT 的公共配置位于 `application.yaml`：

```yaml
jwt:
  expire: 7200000
  header: Authorization
  prefix: "Bearer "
```

### 9.4 启动项目

使用 Maven Wrapper：

```bash
./mvnw spring-boot:run
```

Windows：

```bash
mvnw.cmd spring-boot:run
```

默认端口：

```text
8080
```

---

## 10. 测试

当前项目包含：

### `JwtUtilTest`

覆盖：

- admin Token 生成
- user Token 生成
- JWT Claims 解析
- 正常 JWT 验证
- 被篡改 JWT 验证
- JWT 过期验证

### `PermissionMapperTest`

用于验证：

```text
userId → permission_code
```

权限查询链路。

运行全部测试：

```bash
./mvnw test
```

---

## 11. 认证与异常响应

### 401 Unauthorized

典型场景：

- 未携带 JWT 访问受保护接口
- JWT 非法
- JWT 签名校验失败
- JWT 已过期

处理入口：

```text
JwtAuthenticationEntryPoint
```

### 403 Forbidden

典型场景：

- 用户已经认证成功
- 但不具有 `@PreAuthorize` 要求的权限

当前方法级权限不足由全局异常处理统一转换为 403。

项目同时保留 `JwtAccessDeniedHandler`，用于处理 Security Filter Chain 层面的授权失败。

---

## 12. 当前项目范围

本项目当前主要用于练习和验证安全框架主链路，因此重点是：

```text
全局异常处理
      +
JWT
      +
Spring Security
      +
RBAC
```

当前项目 **尚未实现完整业务登录模块**，例如：

- 登录 Controller
- 用户名 / 密码认证流程
- BCrypt 密码校验
- Refresh Token
- Redis Token 状态管理
- Token 主动注销
- 用户、角色、权限的 CRUD 管理

目前 JWT 主要通过测试代码生成，用于验证后续 Security + RBAC 请求链路。

因此本项目更适合作为：

> Spring Security 认证授权机制的独立练习项目与基础模板，而不是完整用户中心或生产级认证服务。

---

## 13. 项目文档

认证、授权、RBAC 和异常处理之间的详细工作流程：

**[SECURITY_AUTH_FLOW.md](./SECURITY_AUTH_FLOW.md)**

核心理解：

```text
JWT
解决“你是谁”
      ↓
Spring Security
管理“当前请求是谁”
      ↓
RBAC
判断“你能做什么”
      ↓
Global Exception Handler
负责“发生异常后如何统一返回”
```
