# AI 大模型智能识别系统

基于 SpringBoot 3 + MyBatis-Flex 的 AI 视频流智能识别系统，支持对接 OpenAI 协议的大模型进行图像识别。

## 功能特点

- 支持多个视频流同时监控
- 支持配置多种识别规则（如老人摔倒检测、人数统计等）
- 支持对接多个大模型 API（OpenAI、DeepSeek 等）
- 灵活的定时任务调度，可为每个视频流设置不同的识别间隔
- 完整的 RESTful API 接口

## 技术栈

- Java 21
- SpringBoot 3.5.8
- MyBatis-Flex 1.9.7
- MySQL 8.0+
- JavaCV (视频流处理)
- OkHttp (HTTP 请求)

## 快速开始

### 1. 数据库准备

执行 `src/main/resources/schema.sql` 创建数据库表结构：

```bash
mysql -u root -p < src/main/resources/schema.sql
```

（可选）导入示例数据：

```bash
mysql -u root -p < src/main/resources/data-example.sql
```

### 2. 配置数据库

编辑 `src/main/resources/application.yml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/aidetect?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### 3. 运行项目

```bash
./mvnw spring-boot:run
```

或者使用 IDE 直接运行 `AidetectApplication.java`

### 4. 访问接口

服务启动后，访问 http://localhost:8080

## API 接口

### 大模型 API 管理

- `GET /api/model/list` - 获取所有大模型配置
- `GET /api/model/{id}` - 获取指定大模型配置
- `POST /api/model` - 创建大模型配置
- `PUT /api/model` - 更新大模型配置
- `DELETE /api/model/{id}` - 删除大模型配置

### 识别规则管理

- `GET /api/rule/list` - 获取所有规则
- `GET /api/rule/{id}` - 获取指定规则
- `POST /api/rule` - 创建规则
- `PUT /api/rule` - 更新规则
- `DELETE /api/rule/{id}` - 删除规则

### 视频流管理

- `GET /api/stream/list` - 获取所有视频流
- `GET /api/stream/{id}` - 获取指定视频流
- `POST /api/stream` - 创建视频流
- `PUT /api/stream` - 更新视频流
- `DELETE /api/stream/{id}` - 删除视频流

### 视频流规则绑定

- `GET /api/stream-rule/list` - 获取所有绑定关系
- `GET /api/stream-rule/{id}` - 获取指定绑定关系
- `POST /api/stream-rule` - 创建绑定关系
- `PUT /api/stream-rule` - 更新绑定关系
- `DELETE /api/stream-rule/{id}` - 删除绑定关系
- `GET /api/stream-rule/by-stream/{streamId}` - 获取指定视频流的所有规则

### 检测日志查询（内存存储）

- `GET /api/logs/rule/{ruleId}?limit=50` - 查询指定规则的运行日志
- `GET /api/logs/stream/{streamId}?limit=50` - 查询指定视频流的运行日志
- `GET /api/logs/all?limit=100` - 查询所有运行日志
- `GET /api/logs/statistics` - 获取统计信息（总数、成功率、平均耗时等）
- `DELETE /api/logs/clear` - 清空所有日志
- `DELETE /api/logs/clear/rule/{ruleId}` - 清空指定规则的日志
- `DELETE /api/logs/clear/stream/{streamId}` - 清空指定视频流的日志

**说明**：
- 日志保存在内存中，重启应用后日志会丢失
- 每个规则最多保留 100 条日志
- 每个视频流最多保留 100 条日志
- 全局最多保留 1000 条日志
- 日志包含：执行时间、状态（SUCCESS/FAIL）、识别结果、错误信息、执行耗时等

## 使用示例

### 1. 配置大模型 API

```json
POST /api/model
{
  "name": "OpenAI",
  "baseUrl": "https://api.openai.com/v1",
  "apiKey": "sk-your-api-key",
  "modelName": "gpt-4o",
  "enabled": 1
}
```

### 2. 创建识别规则

```json
POST /api/rule
{
  "modelApiId": 1,
  "name": "老人摔倒检测",
  "description": "检测图像中是否有老人摔倒",
  "promptTemplate": "请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {\"has_fall\": true/false, \"confidence\": 0-100, \"description\": \"描述\"}",
  "enabled": 1
}
```

### 3. 添加视频流

```json
POST /api/stream
{
  "name": "家庭客厅摄像头",
  "streamUrl": "rtsp://192.168.1.100:554/stream1",
  "enabled": 1,
  "intervalSeconds": 5
}
```

### 4. 绑定规则到视频流

```json
POST /api/stream-rule
{
  "videoStreamId": 1,
  "aiRuleId": 1,
  "enabled": 1
}
```

### 5. 查询运行日志

```bash
# 查询规则 1 的最近 20 条日志
GET /api/logs/rule/1?limit=20

# 查询视频流 1 的所有日志
GET /api/logs/stream/1

# 查询所有日志
GET /api/logs/all?limit=50

# 查询统计信息
GET /api/logs/statistics
```

返回示例：
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

## 工作原理

1. 系统每秒扫描一次所有启用的视频流
2. 对于每个视频流，检查是否到达识别间隔时间
3. 如果到达间隔时间，从视频流中捕获一帧图像
4. 将图像提交到该视频流绑定的所有规则对应的大模型进行识别
5. 大模型返回 JSON 格式的识别结果
6. 系统记录识别结果日志

## 项目结构

```
src/main/java/com/z1812/aidetect/
├── entity/          # 实体类
├── mapper/          # MyBatis Mapper
├── service/         # 服务层
├── controller/      # 控制器
├── dto/             # 数据传输对象
├── config/          # 配置类
├── task/            # 定时任务
└── util/            # 工具类
    ├── OpenAIClient.java    # OpenAI 协议调用
    └── VideoCapture.java    # 视频流截图
```

## 注意事项

1. 视频流 URL 需要支持 RTSP 或 HTTP 协议
2. 大模型需要支持 OpenAI 格式的图像识别 API
3. 建议为不同的识别任务配置不同的间隔时间，避免过于频繁的识别
4. JavaCV 依赖较大，首次编译可能需要较长时间

## 许可证

MIT License
