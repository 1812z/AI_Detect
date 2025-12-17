package com.z1812.ai_detect.task;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.entity.VideoStream;
import com.z1812.ai_detect.entity.VideoStreamRule;
import com.z1812.ai_detect.service.AiDetectionService;
import com.z1812.ai_detect.service.AiRuleService;
import com.z1812.ai_detect.service.DetectionLogService;
import com.z1812.ai_detect.service.VideoStreamRuleService;
import com.z1812.ai_detect.service.VideoStreamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class VideoDetectionTask {

    private final VideoStreamService videoStreamService;
    private final VideoStreamRuleService videoStreamRuleService;
    private final AiRuleService aiRuleService;
    private final AiDetectionService aiDetectionService;
    private final DetectionLogService detectionLogService;
    private final ObjectMapper objectMapper;

    private final Map<Long, Long> lastExecutionTime = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 1000)
    public void detectAllStreams() {
        List<VideoStream> streams = videoStreamService.listEnabled();

        if (streams.isEmpty()) {
            log.warn("没有找到启用的视频流，请先添加视频流配置");
            return;
        }

        log.info("扫描视频流，共 {} 个已启用", streams.size());

        for (VideoStream stream : streams) {
            try {
                long currentTime = System.currentTimeMillis();
                Long lastTime = lastExecutionTime.get(stream.getId());

                if (lastTime != null &&
                    (currentTime - lastTime) < stream.getIntervalSeconds() * 1000L) {
                    log.debug("视频流 [{}] 未到识别间隔时间，跳过", stream.getName());
                    continue;
                }

                log.info("开始处理视频流: [{}], URL: {}", stream.getName(), stream.getStreamUrl());
                lastExecutionTime.put(stream.getId(), currentTime);

                List<VideoStreamRule> streamRules = videoStreamRuleService.listByVideoStreamId(stream.getId());

                if (streamRules.isEmpty()) {
                    log.warn("视频流 [{}] 没有绑定任何识别规则", stream.getName());
                    continue;
                }

                log.info("视频流 [{}] 绑定了 {} 个规则", stream.getName(), streamRules.size());

                for (VideoStreamRule streamRule : streamRules) {
                    AiRule rule = aiRuleService.getById(streamRule.getAiRuleId());
                    if (rule == null) {
                        log.warn("规则ID {} 不存在", streamRule.getAiRuleId());
                        continue;
                    }
                    if (rule.getEnabled() != 1) {
                        log.warn("规则 [{}] 未启用", rule.getName());
                        continue;
                    }

                    long startTime = System.currentTimeMillis();
                    try {
                        log.info("执行识别 - 视频流: [{}], 规则: [{}]", stream.getName(), rule.getName());
                        String result = aiDetectionService.detectByRule(stream.getStreamUrl(), rule);
                        long duration = System.currentTimeMillis() - startTime;

                        log.info("检测完成 - 视频流: [{}], 规则: [{}], 耗时: {}ms, 结果: {}",
                                stream.getName(), rule.getName(), duration, result);

                        // 尝试从 AI 结果中提取 success 字段
                        Integer aiSuccess = extractSuccessFromResult(result);

                        detectionLogService.addLog(
                                stream.getId(),
                                rule.getId(),
                                streamRule.getId(),
                                "SUCCESS",
                                result,
                                aiSuccess,
                                null,
                                (int) duration
                        );
                    } catch (Exception e) {
                        long duration = System.currentTimeMillis() - startTime;
                        log.error("检测失败 - 视频流: [{}], 规则: [{}], 错误: {}",
                                stream.getName(), rule.getName(), e.getMessage(), e);

                        detectionLogService.addLog(
                                stream.getId(),
                                rule.getId(),
                                streamRule.getId(),
                                "FAILURE",
                                null,
                                null,
                                e.getMessage(),
                                (int) duration
                        );
                    }
                }

            } catch (Exception e) {
                log.error("处理视频流失败: [{}], 错误: {}", stream.getName(), e.getMessage(), e);
            }
        }
    }

    /**
     * 从 AI 结果中提取 success 字段
     * 尝试解析 JSON 并提取 success 字段，支持布尔值和数字
     */
    private Integer extractSuccessFromResult(String result) {
        if (result == null || result.trim().isEmpty()) {
            return null;
        }

        try {
            JsonNode jsonNode = objectMapper.readTree(result);

            // 尝试获取 success 字段
            if (jsonNode.has("success")) {
                JsonNode successNode = jsonNode.get("success");

                // 如果是布尔值
                if (successNode.isBoolean()) {
                    return successNode.asBoolean() ? 1 : 0;
                }

                // 如果是数字
                if (successNode.isNumber()) {
                    return successNode.asInt();
                }

                // 如果是字符串，尝试转换
                if (successNode.isTextual()) {
                    String text = successNode.asText().toLowerCase();
                    if ("true".equals(text) || "1".equals(text)) {
                        return 1;
                    } else if ("false".equals(text) || "0".equals(text)) {
                        return 0;
                    }
                }
            }
        } catch (Exception e) {
            log.debug("无法从结果中提取 success 字段: {}", e.getMessage());
        }

        return null;
    }
}
