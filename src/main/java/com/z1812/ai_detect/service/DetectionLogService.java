package com.z1812.ai_detect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.entity.DetectionLog;
import com.z1812.ai_detect.entity.VideoStream;
import com.z1812.ai_detect.mapper.DetectionLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.z1812.ai_detect.entity.table.DetectionLogTableDef.DETECTION_LOG;

// 检测结果管理服务

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectionLogService {

    private final DetectionLogMapper detectionLogMapper;
    private final VideoStreamService videoStreamService;
    private final AiRuleService aiRuleService;

    /**
     * 添加检测日志
     */
    public DetectionLog addLog(Long videoStreamId, Long aiRuleId, Long videoStreamRuleId,
                               String status, String aiResult, Integer aiSuccess,
                               String errorMessage, Integer executionTime) {
        DetectionLog log = new DetectionLog();
        log.setVideoStreamId(videoStreamId);
        log.setAiRuleId(aiRuleId);
        log.setVideoStreamRuleId(videoStreamRuleId);
        log.setStatus(status);
        log.setAiResult(aiResult);
        log.setAiSuccess(aiSuccess);
        log.setErrorMessage(errorMessage);
        log.setExecutionTime(executionTime);
        log.setCreatedAt(LocalDateTime.now());

        detectionLogMapper.insert(log);
        return log;
    }

    /**
     * 根据规则ID获取日志（返回DTO）
     */
    public List<com.z1812.ai_detect.dto.DetectionLog> getLogsByRuleId(Long ruleId, Integer limit) {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(DETECTION_LOG.AI_RULE_ID.eq(ruleId))
                .orderBy(DETECTION_LOG.CREATED_AT.desc());

        if (limit != null && limit > 0) {
            wrapper.limit(limit);
        }

        List<DetectionLog> entities = detectionLogMapper.selectListByQuery(wrapper);
        return entities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * 获取指定时间范围内的日志
     */
    public List<DetectionLog> getLogsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(DETECTION_LOG.CREATED_AT.ge(startTime))
                .and(DETECTION_LOG.CREATED_AT.le(endTime))
                .orderBy(DETECTION_LOG.CREATED_AT.desc());

        return detectionLogMapper.selectListByQuery(wrapper);
    }

    /**
     * 根据视频流ID获取日志
     */
    public List<DetectionLog> getLogsByStreamId(Long streamId, Integer limit) {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(DETECTION_LOG.VIDEO_STREAM_ID.eq(streamId))
                .orderBy(DETECTION_LOG.CREATED_AT.desc());

        if (limit != null && limit > 0) {
            wrapper.limit(limit);
        }

        return detectionLogMapper.selectListByQuery(wrapper);
    }

    /**
     * 将 Entity 转换为 DTO
     */
    private com.z1812.ai_detect.dto.DetectionLog convertToDto(DetectionLog entity) {
        VideoStream stream = videoStreamService.getById(entity.getVideoStreamId());
        AiRule rule = aiRuleService.getById(entity.getAiRuleId());

        return com.z1812.ai_detect.dto.DetectionLog.builder()
                .id(entity.getId())
                .videoStreamId(entity.getVideoStreamId())
                .videoStreamName(stream != null ? stream.getName() : "Unknown")
                .ruleId(entity.getAiRuleId())
                .ruleName(rule != null ? rule.getName() : "Unknown")
                .status(entity.getStatus())
                .result(entity.getAiResult())
                .errorMessage(entity.getErrorMessage())
                .createTime(entity.getCreatedAt())
                .duration(entity.getExecutionTime() != null ? entity.getExecutionTime().longValue() : 0L)
                .build();
    }
}
