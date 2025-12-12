# 认证 API 文档

## 概述

本系统采用基于 Token 的认证机制，用户需要先通过密码登录获取 Token，然后在后续请求中携带 Token 访问受保护的 API。

## 配置说明

在 `application.yml` 中配置认证参数：

```yaml
auth:
  # 登录密码 (请修改为自己的密码)
  password: admin123
  # token过期时间(分钟)，0表示永不过期
  token-expire-minutes: 0
```

**重要提示：** 部署到生产环境前，请务必修改默认密码！

## API 接口

### 1. 登录

**接口地址：** `POST /api/auth/login`

**请求头：** 无需认证

**请求体：**
```json
{
  "password": "admin123"
}
```

**成功响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

**失败响应：**
```json
{
  "code": 401,
  "message": "密码错误",
  "data": null
}
```

### 2. 登出

**接口地址：** `POST /api/auth/logout`

**请求头：**
```
Authorization: Bearer {token}
```

**请求体：** 无

**成功响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 3. 检查登录状态

**接口地址：** `GET /api/auth/check`

**请求头：**
```
Authorization: Bearer {token}
```

**成功响应：**
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**失败响应（未登录或 Token 无效）：**
```json
{
  "code": 401,
  "message": "认证信息无效或已过期",
  "data": null
}
```

## 使用示例

### 登录流程

1. **调用登录接口获取 Token**

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"password":"admin123"}'
```

2. **使用返回的 Token 访问其他 API**

```bash
curl -X GET http://localhost:8080/api/rule/list \
  -H "Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000"
```

3. **登出（可选）**

```bash
curl -X POST http://localhost:8080/api/auth/logout \
  -H "Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000"
```

### JavaScript 示例

```javascript
// 1. 登录
async function login(password) {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ password })
  });

  const result = await response.json();
  if (result.code === 200) {
    // 保存 token 到 localStorage
    localStorage.setItem('token', result.data.token);
    return result.data.token;
  } else {
    throw new Error(result.message);
  }
}

// 2. 访问受保护的 API
async function fetchProtectedData() {
  const token = localStorage.getItem('token');

  const response = await fetch('http://localhost:8080/api/rule/list', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  const result = await response.json();

  // 如果返回 401，说明未登录或 token 过期
  if (result.code === 401) {
    // 跳转到登录页面
    window.location.href = '/login';
    return;
  }

  return result.data;
}

// 3. 登出
async function logout() {
  const token = localStorage.getItem('token');

  await fetch('http://localhost:8080/api/auth/logout', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  // 清除本地 token
  localStorage.removeItem('token');

  // 跳转到登录页面
  window.location.href = '/login';
}
```

## 路径拦截规则

- **需要认证的路径：** `/api/**`（除了以下排除路径）
- **无需认证的路径：**
  - `/api/auth/login` - 登录接口
  - `/api/auth/logout` - 登出接口

所有其他 `/api/**` 路径都需要在请求头中携带有效的 Token。

## 错误代码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 请求成功 |
| 400 | 请求参数错误（如密码为空） |
| 401 | 未授权（密码错误、Token 无效或过期） |
| 500 | 服务器内部错误 |

## Token 过期说明

- Token 过期时间在 `application.yml` 中配置
- 设置为 `0` 表示永不过期
- Token 过期后需要重新登录获取新的 Token
- 建议生产环境设置合理的过期时间（如 30 分钟或 1 小时）

## 安全建议

1. **修改默认密码：** 部署前务必修改 `application.yml` 中的默认密码
2. **使用 HTTPS：** 生产环境建议使用 HTTPS 防止 Token 被窃取
3. **设置过期时间：** 建议设置合理的 Token 过期时间
4. **前端存储：** Token 建议存储在 localStorage 或 sessionStorage 中
5. **密码强度：** 使用强密码，包含大小写字母、数字和特殊字符
6. **定期更换：** 定期更换登录密码

## 常见问题

**Q: 如何修改登录密码？**
A: 编辑 `src/main/resources/application.yml` 文件，修改 `auth.password` 的值，重启应用即可。

**Q: Token 存储在哪里？**
A: Token 存储在服务器内存中（ConcurrentHashMap），重启服务后所有 Token 会失效。

**Q: 如何设置 Token 永不过期？**
A: 在 `application.yml` 中设置 `auth.token-expire-minutes: 0`。

**Q: 前端如何判断是否已登录？**
A: 可以调用 `/api/auth/check` 接口检查当前 Token 是否有效，或者在调用其他 API 时根据返回的状态码判断。

**Q: 多个用户可以同时登录吗？**
A: 可以，每次登录都会生成一个新的 Token，多个 Token 可以同时有效。
