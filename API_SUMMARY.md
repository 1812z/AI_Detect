# AI 识别系统 API 文档

## API 概览

| API 地址 | 说明 | 认证要求 |
|---------|------|----------|
| `POST /api/auth/login` | 登录获取 Token | 无需认证 |
| `POST /api/auth/logout` | 登出 | 需要 Token |
| `GET /api/auth/check` | 检查登录状态 | 需要 Token |
| `GET /api/model/list` | 获取所有大模型配置 | 需要 Token |
| `GET /api/model/{id}` | 获取指定大模型配置 | 需要 Token |
| `POST /api/model` | 创建大模型配置 | 需要 Token |
| `PUT /api/model` | 更新大模型配置 | 需要 Token |
| `DELETE /api/model/{id}` | 删除大模型配置 | 需要 Token |
| `GET /api/rule/list` | 获取所有识别规则 | 需要 Token |
| `GET /api/rule/{id}` | 获取指定识别规则 | 需要 Token |
| `POST /api/rule` | 创建识别规则 | 需要 Token |
| `PUT /api/rule` | 更新识别规则 | 需要 Token |
| `DELETE /api/rule/{id}` | 删除识别规则 | 需要 Token |
| `GET /api/stream/list` | 获取所有视频流 | 需要 Token |
| `GET /api/stream/{id}` | 获取指定视频流 | 需要 Token |
| `POST /api/stream` | 创建视频流 | 需要 Token |
| `PUT /api/stream` | 更新视频流 | 需要 Token |
| `DELETE /api/stream/{id}` | 删除视频流 | 需要 Token |
| `GET /api/stream-rule/list` | 获取所有绑定关系 | 需要 Token |
| `GET /api/stream-rule/{id}` | 获取指定绑定关系 | 需要 Token |
| `POST /api/stream-rule` | 创建绑定关系 | 需要 Token |
| `PUT /api/stream-rule` | 更新绑定关系 | 需要 Token |
| `DELETE /api/stream-rule/{id}` | 删除绑定关系 | 需要 Token |
| `GET /api/stream-rule/by-stream/{streamId}` | 获取指定视频流的所有规则 | 需要 Token |
| `GET /api/logs/{ruleId}` | 查询指定规则的历史执行记录 | 需要 Token |
| `GET /api/statistics/summary` | 获取统计摘要（饼图数据） | 需要 Token |
| `GET /api/statistics/trend` | 获取趋势数据（折线图数据） | 需要 Token |
| `GET /api/test/capture/{streamId}` | 测试视频流捕获（通过 ID） | 需要 Token |
| `GET /api/test/detect/{streamId}/{ruleId}` | 测试 AI 识别（通过 ID） | 需要 Token |
| `POST /api/test/capture-url` | 测试视频流捕获（通过 URL） | 需要 Token |

## 基础信息

- **Base URL**: `http://localhost:8080`
- **响应格式**: JSON
- **字符编码**: UTF-8
- **认证方式**: Bearer Token（除登录接口外，所有 `/api/**` 接口都需要认证）

## 通用响应格式

所有接口返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`: 状态码，200 表示成功，401 表示未授权，500 表示失败
- `message`: 响应消息
- `data`: 响应数据

---