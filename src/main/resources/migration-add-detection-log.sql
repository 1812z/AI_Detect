-- 数据库迁移脚本：添加检测日志表
-- 执行时间：2024-XX-XX
-- 说明：为现有数据库添加 detection_log 表，用于记录AI检测日志

USE aidetect;

-- 创建检测日志表
CREATE TABLE IF NOT EXISTS detection_log (
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI检测日志表';

-- 验证表是否创建成功
SELECT 'detection_log 表创建成功' AS message;
