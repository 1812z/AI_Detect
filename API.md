# AI 识别系统 API 文档

## 基础信息

- **Base URL**: `http://localhost:8080`
- **响应格式**: JSON
- **字符编码**: UTF-8

## 通用响应格式

所有接口返回统一的 JSON 格式：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

- `code`: 状态码，200 表示成功，500 表示失败
- `message`: 响应消息
- `data`: 响应数据

---

## 1. 大模型 API 管理

### 1.1 获取所有大模型配置

**接口**: `GET /api/model/list`

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

**请求头**: `Content-Type: application/json`

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

**请求头**: `Content-Type: application/json`

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

**路径参数**:
- `id`: 规则 ID

**返回示例**: 同 2.1 中的单个数据对象

### 2.3 创建识别规则

**接口**: `POST /api/rule`

**请求头**: `Content-Type: application/json`

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

**请求体**: 同 2.3，需包含 `id` 字段

### 2.5 删除识别规则

**接口**: `DELETE /api/rule/{id}`

**路径参数**:
- `id`: 规则 ID

---

## 3. 视频流管理

### 3.1 获取所有视频流

**接口**: `GET /api/stream/list`

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

**路径参数**:
- `id`: 视频流 ID

**返回示例**: 同 3.1 中的单个数据对象

### 3.3 创建视频流

**接口**: `POST /api/stream`

**请求头**: `Content-Type: application/json`

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

**请求体**: 同 3.3，需包含 `id` 字段

### 3.5 删除视频流

**接口**: `DELETE /api/stream/{id}`

**路径参数**:
- `id`: 视频流 ID

---

## 4. 视频流规则绑定管理

### 4.1 获取所有绑定关系

**接口**: `GET /api/stream-rule/list`

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

**路径参数**:
- `id`: 绑定关系 ID

### 4.3 创建绑定关系

**接口**: `POST /api/stream-rule`

**请求头**: `Content-Type: application/json`

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

**请求体**: 同 4.3，需包含 `id` 字段

### 4.5 删除绑定关系

**接口**: `DELETE /api/stream-rule/{id}`

**路径参数**:
- `id`: 绑定关系 ID

### 4.6 获取指定视频流的所有规则

**接口**: `GET /api/stream-rule/by-stream/{streamId}`

**路径参数**:
- `streamId`: 视频流 ID

**返回示例**: 同 4.1

---

## 5. 检测日志查询（内存存储）

### 5.1 查询指定规则的历史执行记录

**接口**: `GET /api/logs/{ruleId}`

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
- 日志保存在内存中，重启应用后会清空
- 每个规则最多保留 100 条日志

---

## 6. 测试接口

### 6.1 测试视频流捕获（通过 ID）

**接口**: `GET /api/test/capture/{streamId}`

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

### 6.2 测试视频流捕获（通过 URL）

**接口**: `POST /api/test/capture-url`

**请求头**: `Content-Type: application/json`

**请求体**:
```json
{
  "url": "http://192.168.44.236:5000/screen"
}
```

**返回示例**: 同 6.1

**说明**: 此接口用于测试视频流是否可以正常捕获图像，返回 Base64 编码的图片数据

---

## 7. 使用流程

### 7.1 完整配置流程

1. **创建大模型 API 配置**
   ```
   POST /api/model
   ```

2. **创建识别规则**
   ```
   POST /api/rule
   ```

3. **创建视频流**
   ```
   POST /api/stream
   ```

4. **绑定规则到视频流**
   ```
   POST /api/stream-rule
   ```

5. **系统自动开始识别**
   - 系统会根据视频流的 `intervalSeconds` 定期执行识别
   - 识别结果会自动记录到内存日志中

6. **查询识别日志**
   ```
   GET /api/logs/{ruleId}
   ```

### 7.2 提示词编写建议

**用户提示词（promptTemplate）**:
```
请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {"has_fall": true/false, "confidence": 0-100, "description": "具体描述"}
```

### 7.3 常见识别规则示例

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

## 8. 错误处理

### 8.1 错误响应格式

```json
{
  "code": 500,
  "message": "错误信息描述",
  "data": null
}
```

### 8.2 常见错误

- **数据库连接失败**: 检查 application.yml 中的数据库配置
- **视频流无法访问**: 检查视频流 URL 是否正确，网络是否可达
- **大模型 API 调用失败**: 检查 API Key 是否正确，余额是否充足
- **图像捕获失败**: 检查视频流格式是否支持（RTSP、HTTP 等）

---

## 9. 注意事项

1. **视频流 URL 格式**:
   - RTSP 流: `rtsp://192.168.1.100:554/stream1`
   - HTTP 图片: `http://192.168.1.100:5000/image.jpg`
   - HTTP-FLV: `http://192.168.1.100:8080/live/stream.flv`
   - HLS (M3U8): `http://192.168.1.100:8080/live/stream.m3u8`

2. **识别间隔设置**:
   - 建议根据实际需求设置，避免过于频繁
   - 一般设置 3-10 秒即可
   - 过短会增加服务器压力和 API 调用成本

3. **大模型选择**:
   - OpenAI GPT-4o: 识别准确度高，成本较高
   - OpenAI GPT-4o-mini: 性价比高，适合一般场景
   - DeepSeek: 成本低，中文支持好

4. **日志管理**:
   - 定期清理日志，避免内存占用过高
   - 重要日志建议导出保存
   - 可通过统计接口监控系统运行状态

5. **系统提示词优化**:
   - 明确告知输出格式（JSON）
   - 强调关注重点（人物、行为、场景等）
   - 避免多余解释，只返回结果

---

## 10. 完整示例

### 示例：配置一个老人摔倒检测系统

**步骤 1: 创建大模型配置**
```bash
curl -X POST http://localhost:8080/api/model \
  -H "Content-Type: application/json" \
  -d '{
    "name": "OpenAI",
    "baseUrl": "https://api.openai.com/v1",
    "apiKey": "sk-your-api-key",
    "modelName": "gpt-4o",
    "enabled": 1
  }'
```

**步骤 2: 创建识别规则**
```bash
curl -X POST http://localhost:8080/api/rule \
  -H "Content-Type: application/json" \
  -d '{
    "modelApiId": 1,
    "name": "老人摔倒检测",
    "description": "检测老年人是否摔倒",
    "promptTemplate": "请分析这张图片，检测是否有老人摔倒在地。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"person_position\": \"站立/坐着/躺地\", \"description\": \"详细描述\"}",
    "enabled": 1
  }'
```

**步骤 3: 创建视频流**
```bash
curl -X POST http://localhost:8080/api/stream \
  -H "Content-Type: application/json" \
  -d '{
    "name": "养老院房间1",
    "streamUrl": "rtsp://192.168.1.100:554/stream1",
    "enabled": 1,
    "intervalSeconds": 5
  }'
```

**步骤 4: 绑定规则**
```bash
curl -X POST http://localhost:8080/api/stream-rule \
  -H "Content-Type: application/json" \
  -d '{
    "videoStreamId": 1,
    "aiRuleId": 1,
    "enabled": 1
  }'
```

**步骤 5: 查询日志**
```bash
# 查询规则的最近 10 条历史记录
curl http://localhost:8080/api/logs/1?limit=10

# 获取最新的执行结果
curl http://localhost:8080/api/logs/1?limit=1
```

---

## 11. 数据库表结构

### 11.1 ai_model_api (大模型配置表)
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

### 11.2 ai_rule (识别规则表)
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

### 11.3 video_stream (视频流表)
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

### 11.4 video_stream_rule (视频流规则绑定表)
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

---

**文档版本**: 1.0
**最后更新**: 2025-12-11
