package com.z1812.ai_detect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.ai_detect.entity.DetectionLog;
import com.z1812.ai_detect.mapper.DetectionLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.z1812.ai_detect.entity.table.DetectionLogTableDef.DETECTION_LOG;

@Slf4j
@Service
@RequiredArgsConstructor
public class DetectionLogService {

    private final DetectionLogMapper detectionLogMapper;

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
     * 根据规则ID获取日志
     */
    public List<DetectionLog> getLogsByRuleId(Long ruleId, Integer limit) {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(DETECTION_LOG.AI_RULE_ID.eq(ruleId))
                .orderBy(DETECTION_LOG.CREATED_AT.desc());

        if (limit != null && limit > 0) {
            wrapper.limit(limit);
        }

        return detectionLogMapper.selectListByQuery(wrapper);
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
}
