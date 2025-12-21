# AI 识别系统 API 文档

## API 概览
| 模块 | 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|------|
| **认证** | POST | `/api/auth/login` | 登录获取 Token | 否 |
|  | POST | `/api/auth/logout` | 登出 | 是 |
|  | GET | `/api/auth/check` | 检查登录状态 | 是 |
| **大模型配置** | GET | `/api/model/list` | 获取列表 | 是 |
|  | GET | `/api/model/{id}` | 获取详情 | 是 |
|  | POST | `/api/model` | 创建 | 是 |
|  | PUT | `/api/model` | 更新 | 是 |
|  | DELETE | `/api/model/{id}` | 删除 | 是 |
| **识别规则** | GET | `/api/rule/list` | 获取列表 | 是 |
|  | GET | `/api/rule/{id}` | 获取详情 | 是 |
|  | POST | `/api/rule` | 创建 | 是 |
|  | PUT | `/api/rule` | 更新 | 是 |
|  | DELETE | `/api/rule/{id}` | 删除 | 是 |
| **视频流** | GET | `/api/stream/list` | 获取列表 | 是 |
|  | GET | `/api/stream/{id}` | 获取详情 | 是 |
|  | POST | `/api/stream` | 创建 | 是 |
|  | PUT | `/api/stream` | 更新 | 是 |
|  | DELETE | `/api/stream/{id}` | 删除 | 是 |
| **绑定关系** | GET | `/api/stream-rule/list` | 获取列表 | 是 |
|  | GET | `/api/stream-rule/{id}` | 获取详情 | 是 |
|  | POST | `/api/stream-rule` | 创建 | 是 |
|  | PUT | `/api/stream-rule` | 更新 | 是 |
|  | DELETE | `/api/stream-rule/{id}` | 删除 | 是 |
|  | GET | `/api/stream-rule/by-stream/{streamId}` | 获取某视频流的所有规则 | 是 |
| **日志与统计** | GET | `/api/logs/{ruleId}` | 查询规则历史记录 | 是 |
|  | GET | `/api/statistics/summary` | 获取统计摘要（饼图） | 是 |
|  | GET | `/api/statistics/trend` | 获取趋势数据（折线图） | 是 |
| **测试接口** | GET | `/api/test/capture/{streamId}` | 测试视频流捕获（ID） | 是 |
|  | POST | `/api/test/capture-url` | 测试视频流捕获（URL） | 是 |
|  | GET | `/api/test/detect/{streamId}/{ruleId}` | 测试 AI 识别 | 是 |
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

## 0. 认证 API

### 0.1 登录获取 Token

**接口**: `POST /api/auth/login`

**请求头**: `Content-Type: application/json`

**请求体**:
```json
{
  "password": "admin123"
}
```

**成功返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "550e8400-e29b-41d4-a716-446655440000"
  }
}
```

**失败返回示例**:
```json
{
  "code": 401,
  "message": "密码错误",
  "data": null
}
```

### 0.2 登出

**接口**: `POST /api/auth/logout`

**请求头**: `Authorization: Bearer {token}`

**请求体**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 0.3 检查登录状态

**接口**: `GET /api/auth/check`

**请求头**: `Authorization: Bearer {token}`

**返回示例**（成功）:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

**返回示例**（失败 - 未登录或 Token 无效）:
```json
{
  "code": 401,
  "message": "认证信息无效或已过期",
  "data": null
}
```

**认证说明**:
- 所有 `/api/**` 接口（除了 `/api/auth/login`）都需要在请求头中携带 `Authorization: Bearer {token}`
- Token 在 `application.yml` 中配置过期时间，0 表示永不过期
- 部署生产环境前务必修改默认密码

---

## 1. 大模型 API 管理

### 1.1 获取所有大模型配置

**接口**: `GET /api/model/list`

**请求头**: `Authorization: Bearer {token}`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "OpenAI",
      "baseUrl": "https://api.openai.com/v1",
      "apiKey": "sk-xxx",
      "modelName": "gpt-4o",
      "enabled": 1,
      "createdAt": "2025-12-11T10:00:00",
      "updatedAt": "2025-12-11T10:00:00"
    }
  ]
}
```

### 1.2 获取指定大模型配置

**接口**: `GET /api/model/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 大模型 ID

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "OpenAI",
    "baseUrl": "https://api.openai.com/v1",
    "apiKey": "sk-xxx",
    "modelName": "gpt-4o",
    "enabled": 1,
    "createdAt": "2025-12-11T10:00:00",
    "updatedAt": "2025-12-11T10:00:00"
  }
}
```

### 1.3 创建大模型配置

**接口**: `POST /api/model`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "name": "OpenAI",
  "baseUrl": "https://api.openai.com/v1",
  "apiKey": "sk-your-api-key",
  "modelName": "gpt-4o",
  "enabled": 1
}
```

**字段说明**:
- `name`: 模型名称（必填）
- `baseUrl`: API 基础地址（必填）
- `apiKey`: API 密钥（必填）
- `modelName`: 模型名称，如 gpt-4o、deepseek-chat（必填）
- `enabled`: 是否启用，1 启用，0 禁用（必填）

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 1.4 更新大模型配置

**接口**: `PUT /api/model`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "id": 1,
  "name": "OpenAI",
  "baseUrl": "https://api.openai.com/v1",
  "apiKey": "sk-your-api-key",
  "modelName": "gpt-4o",
  "enabled": 1
}
```

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 1.5 删除大模型配置

**接口**: `DELETE /api/model/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 大模型 ID

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

---

## 2. 识别规则管理

### 2.1 获取所有识别规则

**接口**: `GET /api/rule/list`

**请求头**: `Authorization: Bearer {token}`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "modelApiId": 1,
      "name": "老人摔倒检测",
      "description": "检测图像中是否有老人摔倒",
      "promptTemplate": "请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"description\": \"描述\"}",
      "enabled": 1,
      "createdAt": "2025-12-11T10:00:00",
      "updatedAt": "2025-12-11T10:00:00"
    }
  ]
}
```

### 2.2 获取指定识别规则

**接口**: `GET /api/rule/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 规则 ID

**返回示例**: 同 2.1 中的单个数据对象

### 2.3 创建识别规则

**接口**: `POST /api/rule`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "modelApiId": 1,
  "name": "老人摔倒检测",
  "description": "检测图像中是否有老人摔倒",
  "promptTemplate": "请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"description\": \"描述\"}",
  "enabled": 1
}
```

**字段说明**:
- `modelApiId`: 绑定的大模型 API ID（必填）
- `name`: 规则名称（必填）
- `description`: 规则描述（可选）
- `promptTemplate`: 提示词模板，用于告诉大模型如何分析图片（必填）
- `enabled`: 是否启用，1 启用，0 禁用（必填）

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 2.4 更新识别规则

**接口**: `PUT /api/rule`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**: 同 2.3，需包含 `id` 字段

### 2.5 删除识别规则

**接口**: `DELETE /api/rule/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 规则 ID

---

## 3. 视频流管理

### 3.1 获取所有视频流

**接口**: `GET /api/stream/list`

**请求头**: `Authorization: Bearer {token}`

**请求参数**: 无

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "name": "家庭客厅摄像头",
      "streamUrl": "rtsp://192.168.1.100:554/stream1",
      "enabled": 1,
      "intervalSeconds": 5,
      "createdAt": "2025-12-11T10:00:00",
      "updatedAt": "2025-12-11T10:00:00"
    }
  ]
}
```

### 3.2 获取指定视频流

**接口**: `GET /api/stream/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 视频流 ID

**返回示例**: 同 3.1 中的单个数据对象

### 3.3 创建视频流

**接口**: `POST /api/stream`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "name": "家庭客厅摄像头",
  "streamUrl": "rtsp://192.168.1.100:554/stream1",
  "enabled": 1,
  "intervalSeconds": 5
}
```

**字段说明**:
- `name`: 视频流名称（必填）
- `streamUrl`: 视频流地址，支持 RTSP、HTTP 等协议（必填）
  - RTSP: `rtsp://ip:port/stream`
  - HTTP 图片: `http://ip:port/image.jpg`
  - HTTP-FLV: `http://ip:port/stream.flv`
  - HLS: `http://ip:port/stream.m3u8`
- `enabled`: 是否启用，1 启用，0 禁用（必填）
- `intervalSeconds`: 识别间隔秒数，每隔多少秒进行一次识别（必填）

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 3.4 更新视频流

**接口**: `PUT /api/stream`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**: 同 3.3，需包含 `id` 字段

### 3.5 删除视频流

**接口**: `DELETE /api/stream/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 视频流 ID

---

## 4. 视频流规则绑定管理

### 4.1 获取所有绑定关系

**接口**: `GET /api/stream-rule/list`

**请求头**: `Authorization: Bearer {token}`

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "videoStreamId": 1,
      "aiRuleId": 1,
      "enabled": 1,
      "createdAt": "2025-12-11T10:00:00"
    }
  ]
}
```

### 4.2 获取指定绑定关系

**接口**: `GET /api/stream-rule/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 绑定关系 ID

### 4.3 创建绑定关系

**接口**: `POST /api/stream-rule`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "videoStreamId": 1,
  "aiRuleId": 1,
  "enabled": 1
}
```

**字段说明**:
- `videoStreamId`: 视频流 ID（必填）
- `aiRuleId`: 识别规则 ID（必填）
- `enabled`: 是否启用，1 启用，0 禁用（必填）

**说明**: 一个视频流可以绑定多个识别规则，每次识别时会依次执行所有绑定的规则

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": null
}
```

### 4.4 更新绑定关系

**接口**: `PUT /api/stream-rule`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**: 同 4.3，需包含 `id` 字段

### 4.5 删除绑定关系

**接口**: `DELETE /api/stream-rule/{id}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `id`: 绑定关系 ID

### 4.6 获取指定视频流的所有规则

**接口**: `GET /api/stream-rule/by-stream/{streamId}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `streamId`: 视频流 ID

**返回示例**: 同 4.1

---

## 5. 检测日志查询（数据库存储）

### 5.1 查询指定规则的历史执行记录

**接口**: `GET /api/logs/{ruleId}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `ruleId`: 规则 ID

**查询参数**:
- `limit`: 返回记录数量，默认 50，可选

**请求示例**: `GET /api/logs/1?limit=20`

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "videoStreamId": 1,
      "videoStreamName": "家庭客厅摄像头",
      "ruleId": 1,
      "ruleName": "老人摔倒检测",
      "status": "SUCCESS",
      "result": "{\"has_fall\": false, \"confidence\": 95, \"description\": \"未检测到摔倒\"}",
      "errorMessage": null,
      "createTime": "2025-12-11T16:30:00",
      "duration": 1250
    }
  ]
}
```

**字段说明**:
- `id`: 日志 ID
- `videoStreamId`: 视频流 ID
- `videoStreamName`: 视频流名称
- `ruleId`: 规则 ID
- `ruleName`: 规则名称
- `status`: 执行状态，SUCCESS 成功，FAIL 失败
- `result`: 识别结果（JSON 字符串）
- `errorMessage`: 错误信息，成功时为 null
- `createTime`: 创建时间
- `duration`: 执行耗时（毫秒）

**使用说明**:
- 返回的数组按时间倒序排列，最新的记录在索引 0 位置
- 可以直接通过 `data[0]` 获取最新的执行结果
- 日志保存在数据库中，永久保存
- 支持通过统计接口进行数据分析

---

## 6. 统计接口

### 6.1 获取统计摘要（饼图数据）

**接口**: `GET /api/statistics/summary`

**请求头**: `Authorization: Bearer {token}`

**查询参数**:
- `timeRange`: 时间范围，可选值 `24h`（24小时）或 `7d`（7天），默认 `24h`
- `successType`: 成功类型，可选值 `execution`（执行成功）或 `ai_result`（AI识别成功），默认 `execution`
- `videoStreamId`: 视频流ID，可选，不传则统计所有视频流
- `ruleId`: 规则ID，可选，不传则统计所有规则

**请求示例**:
```bash
# 查询最近24小时的执行统计
GET /api/statistics/summary?timeRange=24h&successType=execution

# 查询最近7天的AI识别统计（特定视频流）
GET /api/statistics/summary?timeRange=7d&successType=ai_result&videoStreamId=1

# 查询特定规则的统计
GET /api/statistics/summary?timeRange=24h&ruleId=1
```

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalCount": 288,
    "successCount": 245,
    "failureCount": 43,
    "successRate": 85.07
  }
}
```

**字段说明**:
- `totalCount`: 总执行次数
- `successCount`: 成功次数
- `failureCount`: 失败次数
- `successRate`: 成功率（百分比，保留两位小数）

**成功类型说明**:
- `execution`: 根据任务执行状态判断，status 为 `SUCCESS` 表示成功
- `ai_result`: 根据 AI 返回 JSON 中的 `success` 字段判断，值为 `true` 或 `1` 表示成功

**使用场景**:
- 前端饼图展示
- 系统运行状态监控
- 性能指标统计

---

### 6.2 获取趋势数据（折线图数据）

**接口**: `GET /api/statistics/trend`

**请求头**: `Authorization: Bearer {token}`

**查询参数**: 同 6.1

**请求示例**:
```bash
# 查询最近24小时的趋势（24个数据点）
GET /api/statistics/trend?timeRange=24h&successType=execution

# 查询最近7天的趋势（7个数据点）
GET /api/statistics/trend?timeRange=7d&successType=ai_result
```

**返回示例（24小时）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "labels": [
      "00:00", "01:00", "02:00", "03:00", "04:00", "05:00",
      "06:00", "07:00", "08:00", "09:00", "10:00", "11:00",
      "12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
      "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    ],
    "totalCounts": [8, 5, 3, 2, 4, 6, 10, 15, 18, 20, 22, 25, 28, 30, 32, 28, 25, 22, 20, 18, 15, 12, 10, 9],
    "successCounts": [7, 4, 3, 2, 3, 5, 9, 13, 16, 18, 20, 22, 25, 27, 29, 25, 22, 20, 18, 16, 13, 10, 9, 8],
    "failureCounts": [1, 1, 0, 0, 1, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 2, 2, 2, 2, 2, 1, 1]
  }
}
```

**返回示例（7天）**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "labels": ["12-11", "12-12", "12-13", "12-14", "12-15", "12-16", "12-17"],
    "totalCounts": [320, 335, 298, 342, 356, 333, 288],
    "successCounts": [280, 300, 265, 310, 320, 300, 245],
    "failureCounts": [40, 35, 33, 32, 36, 33, 43]
  }
}
```

**字段说明**:
- `labels`: 时间标签数组
  - 24小时模式：24个元素，格式为 `HH:00`（如：`00:00`, `01:00`）
  - 7天模式：7个元素，格式为 `MM-dd`（如：`12-11`, `12-12`）
- `totalCounts`: 对应时间点的总执行次数数组
- `successCounts`: 对应时间点的成功次数数组
- `failureCounts`: 对应时间点的失败次数数组

**使用场景**:
- 前端折线图展示
- 趋势分析
- 系统负载监控

---

### 6.3 统计接口使用示例

**场景1：监控系统整体运行状况**
```bash
# 获取最近24小时的整体统计
curl "http://localhost:8080/api/statistics/summary?timeRange=24h&successType=execution" \
  -H "Authorization: Bearer your-token"

# 获取最近24小时的趋势
curl "http://localhost:8080/api/statistics/trend?timeRange=24h&successType=execution" \
  -H "Authorization: Bearer your-token"
```

**场景2：分析特定视频流的识别效果**
```bash
# 统计视频流ID为1的最近7天AI识别成功率
curl "http://localhost:8080/api/statistics/summary?timeRange=7d&successType=ai_result&videoStreamId=1" \
  -H "Authorization: Bearer your-token"
```

**场景3：评估某个规则的表现**
```bash
# 统计规则ID为2的最近24小时表现
curl "http://localhost:8080/api/statistics/summary?timeRange=24h&ruleId=2" \
  -H "Authorization: Bearer your-token"
curl "http://localhost:8080/api/statistics/trend?timeRange=24h&ruleId=2" \
  -H "Authorization: Bearer your-token"
```

**前端集成示例（Vue + ECharts）**:
```javascript
// 获取饼图数据
async function loadPieChart() {
  const token = localStorage.getItem('token');
  const response = await fetch('/api/statistics/summary?timeRange=24h&successType=execution', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  const result = await response.json();
  const { successCount, failureCount } = result.data;

  // ECharts 配置
  const option = {
    series: [{
      type: 'pie',
      data: [
        { value: successCount, name: '成功' },
        { value: failureCount, name: '失败' }
      ]
    }]
  };
  chart.setOption(option);
}

// 获取折线图数据
async function loadLineChart() {
  const token = localStorage.getItem('token');
  const response = await fetch('/api/statistics/trend?timeRange=24h&successType=execution', {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });
  const result = await response.json();
  const { labels, totalCounts, successCounts, failureCounts } = result.data;

  // ECharts 配置
  const option = {
    xAxis: { type: 'category', data: labels },
    yAxis: { type: 'value' },
    series: [
      { name: '总数', type: 'line', data: totalCounts },
      { name: '成功', type: 'line', data: successCounts },
      { name: '失败', type: 'line', data: failureCounts }
    ]
  };
  chart.setOption(option);
}
```

---

## 7. 测试接口

### 7.1 测试视频流捕获（通过 ID）

**接口**: `GET /api/test/capture/{streamId}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `streamId`: 视频流 ID

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "streamName": "家庭客厅摄像头",
    "streamUrl": "rtsp://192.168.1.100:554/stream1",
    "imageSize": 45678,
    "imageBase64": "iVBORw0KGgoAAAANSUhEUgAA...",
    "message": "成功捕获图像"
  }
}
```

### 7.2 测试 AI 识别（通过 ID）

**接口**: `GET /api/test/detect/{streamId}/{ruleId}`

**请求头**: `Authorization: Bearer {token}`

**路径参数**:
- `streamId`: 视频流 ID
- `ruleId`: 规则 ID

**返回示例**:
```json
{
  "code": 200,
  "message": "success",
  "data": "{\"has_fall\": false, \"confidence\": 95, \"description\": \"未检测到摔倒\"}"
}
```

**说明**: 此接口用于测试指定视频流和规则的完整识别流程，返回 AI 的原始 JSON 结果

### 7.3 测试视频流捕获（通过 URL）

**接口**: `POST /api/test/capture-url`

**请求头**: 
- `Content-Type: application/json`
- `Authorization: Bearer {token}`

**请求体**:
```json
{
  "url": "http://192.168.44.236:5000/screen"
}
```

**返回示例**: 同 7.1

**说明**: 此接口用于测试视频流是否可以正常捕获图像，返回 Base64 编码的图片数据

---

## 8. 使用流程

### 8.1 完整配置流程

1. **登录获取 Token**
   ```
   POST /api/auth/login
   ```

2. **创建大模型 API 配置**
   ```
   POST /api/model
   ```

3. **创建识别规则**
   ```
   POST /api/rule
   ```

4. **创建视频流**
   ```
   POST /api/stream
   ```

5. **绑定规则到视频流**
   ```
   POST /api/stream-rule
   ```

6. **系统自动开始识别**
   - 系统会根据视频流的 `intervalSeconds` 定期执行识别
   - 识别结果会自动保存到数据库

7. **查询识别日志和统计**
   ```
   GET /api/logs/{ruleId}
   GET /api/statistics/summary
   GET /api/statistics/trend
   ```

### 8.2 提示词编写建议

**用户提示词（promptTemplate）**:
```
请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {"has_fall": true/false, "confidence": 0-100, "description": "具体描述"}
```

### 8.3 常见识别规则示例

**老人摔倒检测**:
```json
{
  "promptTemplate": "请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"description\": \"描述\"}"
}
```

**人数统计**:
```json
{
  "promptTemplate": "请分析这张图片，统计图片中有多少人。返回 JSON 格式: {\"person_count\": 数字, \"description\": \"描述\"}"
}
```

**异常行为检测**:
```json
{
  "promptTemplate": "请分析这张图片，检测是否有异常行为（如打架、奔跑等）。返回 JSON 格式: {\"has_abnormal\": true/false, \"behavior_type\": \"行为类型\", \"description\": \"描述\"}"
}
```

**火灾烟雾检测**:
```json
{
  "promptTemplate": "请分析这张图片，检测是否有火灾或烟雾。返回 JSON 格式: {\"has_fire\": true/false, \"has_smoke\": true/false, \"confidence\": 0-100, \"description\": \"描述\"}"
}
```

---

## 9. 错误处理

### 9.1 错误响应格式

```json
{
  "code": 500,
  "message": "错误信息描述",
  "data": null
}
```

### 9.2 常见错误

- **401 未授权**: 未提供 Token 或 Token 无效/过期
- **数据库连接失败**: 检查 application.yml 中的数据库配置
- **视频流无法访问**: 检查视频流 URL 是否正确，网络是否可达
- **大模型 API 调用失败**: 检查 API Key 是否正确，余额是否充足
- **图像捕获失败**: 检查视频流格式是否支持（RTSP、HTTP 等）

---

## 10. 注意事项

1. **认证要求**: 除登录接口外，所有 API 都需要在请求头中携带有效的 Bearer Token
2. **视频流 URL 格式**:
   - RTSP 流: `rtsp://192.168.1.100:554/stream1`
   - HTTP 图片: `http://192.168.1.100:5000/image.jpg`
   - HTTP-FLV: `http://192.168.1.100:8080/live/stream.flv`
   - HLS (M3U8): `http://192.168.1.100:8080/live/stream.m3u8`
3. **识别间隔设置**:
   - 建议根据实际需求设置，避免过于频繁
   - 一般设置 3-10 秒即可
   - 过短会增加服务器压力和 API 调用成本
4. **大模型选择**:
   - OpenAI GPT-4o: 识别准确度高，成本较高
   - OpenAI GPT-4o-mini: 性价比高，适合一般场景
   - DeepSeek: 成本低，中文支持好
5. **日志管理**:
   - 日志永久保存在数据库中
   - 可通过统计接口监控系统运行状态
   - 建议定期备份数据库
6. **系统提示词优化**:
   - 明确告知输出格式（JSON）
   - 强调关注重点（人物、行为、场景等）
   - 避免多余解释，只返回结果
   - 如需统计成功率，建议在返回 JSON 中包含 `success` 字段

---

## 11. 完整示例

### 示例：配置一个老人摔倒检测系统

**步骤 1: 登录获取 Token**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"password":"admin123"}'
```

**步骤 2: 创建大模型配置**
```bash
curl -X POST http://localhost:8080/api/model \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "name": "OpenAI",
    "baseUrl": "https://api.openai.com/v1",
    "apiKey": "sk-your-api-key",
    "modelName": "gpt-4o",
    "enabled": 1
  }'
```

**步骤 3: 创建识别规则**
```bash
curl -X POST http://localhost:8080/api/rule \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "modelApiId": 1,
    "name": "老人摔倒检测",
    "description": "检测老年人是否摔倒",
    "promptTemplate": "请分析这张图片，检测是否有老人摔倒在地。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"person_position\": \"站立/坐着/躺地\", \"description\": \"详细描述\"}",
    "enabled": 1
  }'
```

**步骤 4: 创建视频流**
```bash
curl -X POST http://localhost:8080/api/stream \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "name": "养老院房间1",
    "streamUrl": "rtsp://192.168.1.100:554/stream1",
    "enabled": 1,
    "intervalSeconds": 5
  }'
```

**步骤 5: 绑定规则**
```bash
curl -X POST http://localhost:8080/api/stream-rule \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer your-token" \
  -d '{
    "videoStreamId": 1,
    "aiRuleId": 1,
    "enabled": 1
  }'
```

**步骤 6: 查询日志**
```bash
# 查询规则的最近 10 条历史记录
curl "http://localhost:8080/api/logs/1?limit=10" \
  -H "Authorization: Bearer your-token"

# 获取最新的执行结果
curl "http://localhost:8080/api/logs/1?limit=1" \
  -H "Authorization: Bearer your-token"
```

**步骤 7: 测试识别**
```bash
# 测试完整的识别流程
curl "http://localhost:8080/api/test/detect/1/1" \
  -H "Authorization: Bearer your-token"
```

---

## 12. 数据库表结构

### 12.1 ai_model_api (大模型配置表)
```sql
CREATE TABLE ai_model_api (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    base_url VARCHAR(300) NOT NULL,
    api_key VARCHAR(200) NOT NULL,
    model_name VARCHAR(100) NOT NULL,
    enabled TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 12.2 ai_rule (识别规则表)
```sql
CREATE TABLE ai_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    model_api_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    prompt_template TEXT NOT NULL,
    enabled TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (model_api_id) REFERENCES ai_model_api(id)
);
```

### 12.3 video_stream (视频流表)
```sql
CREATE TABLE video_stream (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    stream_url VARCHAR(500) NOT NULL,
    enabled TINYINT DEFAULT 1,
    interval_seconds INT DEFAULT 5,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### 12.4 video_stream_rule (视频流规则绑定表)
```sql
CREATE TABLE video_stream_rule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    video_stream_id BIGINT NOT NULL,
    ai_rule_id BIGINT NOT NULL,
    enabled TINYINT DEFAULT 1,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (video_stream_id) REFERENCES video_stream(id),
    FOREIGN KEY (ai_rule_id) REFERENCES ai_rule(id)
);
```

### 12.5 detection_log (检测日志表)
```sql
CREATE TABLE detection_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    video_stream_id BIGINT NOT NULL COMMENT '视频流ID',
    ai_rule_id BIGINT NOT NULL COMMENT '识别规则ID',
    video_stream_rule_id BIGINT COMMENT '视频流规则绑定ID',
    status VARCHAR(20) NOT NULL COMMENT '执行状态: SUCCESS/FAILURE',
    ai_result TEXT COMMENT 'AI返回的完整JSON结果',
    ai_success TINYINT COMMENT 'AI识别结果中的success字段',
    error_message TEXT COMMENT '错误信息（如果失败）',
    execution_time INT COMMENT '执行耗时（毫秒）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
    FOREIGN KEY (video_stream_id) REFERENCES video_stream(id),
    FOREIGN KEY (ai_rule_id) REFERENCES ai_rule(id),
    FOREIGN KEY (video_stream_rule_id) REFERENCES video_stream_rule(id),
    INDEX idx_created_at (created_at),
    INDEX idx_video_stream_id (video_stream_id),
    INDEX idx_status (status)
);
```

**字段说明**:
- `video_stream_id`: 关联的视频流ID
- `ai_rule_id`: 关联的识别规则ID
- `video_stream_rule_id`: 关联的绑定关系ID
- `status`: 执行状态，`SUCCESS` 表示执行成功，`FAILURE` 表示执行失败
- `ai_result`: AI 返回的完整 JSON 结果字符串
- `ai_success`: 从 AI 结果中提取的 success 字段（1表示成功，0表示失败，null表示未提取）
- `error_message`: 错误信息（执行失败时记录）
- `execution_time`: 执行耗时（毫秒）
- `created_at`: 执行时间，用于统计和查询

---

**文档版本**: 3.0
**最后更新**: 2025-12-19
**更新内容**:
- 添加完整的认证 API 文档
- 新增测试接口 `/api/test/detect/{streamId}/{ruleId}`
- 所有 API 接口添加认证要求说明
- 更新检测日志字段描述
- 完善统计接口参数说明
- 更新完整使用示例，包含认证步骤