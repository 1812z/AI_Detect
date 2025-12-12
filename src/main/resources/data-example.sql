-- 示例数据，仅供参考

USE aidetect;

-- 插入示例大模型 API 配置
INSERT INTO ai_model_api (name, base_url, api_key, model_name, enabled) VALUES
('OpenAI', 'https://api.openai.com/v1', 'your-openai-api-key', 'gpt-4o', 1),
('DeepSeek', 'https://api.deepseek.com/v1', 'your-deepseek-api-key', 'deepseek-chat', 1);

-- 插入示例识别规则
INSERT INTO ai_rule (model_api_id, name, description, prompt_template, enabled) VALUES
(1, '老人摔倒检测', '检测图像中是否有老人摔倒',
'请分析这张图片，检测是否有老人摔倒。返回 JSON 格式: {"has_fall": true/false, "confidence": 0-100, "description": "描述"}', 1),

(1, '人数统计', '统计图像中的人数',
'请分析这张图片，统计图片中有多少人。返回 JSON 格式: {"person_count": 数字, "description": "描述"}', 1),

(2, '异常行为检测', '检测图像中是否有异常行为',
'请分析这张图片，检测是否有异常行为（如打架、奔跑等）。返回 JSON 格式: {"has_abnormal": true/false, "behavior_type": "行为类型", "description": "描述"}', 1);

-- 插入示例视频流
INSERT INTO video_stream (name, stream_url, enabled, interval_seconds) VALUES
('家庭客厅摄像头', 'rtsp://192.168.1.100:554/stream1', 1, 5),
('养老院房间1', 'rtsp://192.168.1.101:554/stream1', 1, 3);

-- 绑定视频流和规则
INSERT INTO video_stream_rule (video_stream_id, ai_rule_id, enabled) VALUES
(1, 1, 1),  -- 家庭客厅摄像头 -> 老人摔倒检测
(1, 2, 1),  -- 家庭客厅摄像头 -> 人数统计
(2, 1, 1),  -- 养老院房间1 -> 老人摔倒检测
(2, 3, 1);  -- 养老院房间1 -> 异常行为检测
