package com.z1812.ai_detect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.ai_detect.dto.StatisticsRequest;
import com.z1812.ai_detect.dto.StatisticsSummaryResponse;
import com.z1812.ai_detect.dto.TrendDataResponse;
import com.z1812.ai_detect.entity.DetectionLog;
import com.z1812.ai_detect.mapper.DetectionLogMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.z1812.ai_detect.entity.table.DetectionLogTableDef.DETECTION_LOG;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DetectionLogMapper detectionLogMapper;

    // 获取统计摘要
    public StatisticsSummaryResponse getSummary(StatisticsRequest request) {
        LocalDateTime startTime = getStartTime(request.getTimeRange());
        LocalDateTime endTime = LocalDateTime.now();

        List<DetectionLog> logs = queryLogs(startTime, endTime, request.getVideoStreamId(), request.getRuleId());

        long totalCount = logs.size();
        long successCount = countSuccess(logs, request.getSuccessType());
        long failureCount = totalCount - successCount;
        double successRate = totalCount > 0 ? (successCount * 100.0 / totalCount) : 0;

        return StatisticsSummaryResponse.builder()
                .totalCount(totalCount)
                .successCount(successCount)
                .failureCount(failureCount)
                .successRate(Math.round(successRate * 100.0) / 100.0)
                .build();
    }


    // 获取趋势数据
    public TrendDataResponse getTrendData(StatisticsRequest request) {
        String timeRange = request.getTimeRange();
        boolean is24Hours = "24h".equalsIgnoreCase(timeRange);
        int points = is24Hours ? 24 : 7;

        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = getStartTime(timeRange);

        List<String> labels = new ArrayList<>();
        List<Long> totalCounts = new ArrayList<>();
        List<Long> successCounts = new ArrayList<>();
        List<Long> failureCounts = new ArrayList<>();

        if (is24Hours) {
            // 24小时：每个点代表1小时
            for (int i = 0; i < points; i++) {
                LocalDateTime pointEnd = endTime.minusHours(points - 1 - i);
                LocalDateTime pointStart = pointEnd.minusHours(1);

                labels.add(pointEnd.format(DateTimeFormatter.ofPattern("HH:00")));

                List<DetectionLog> logs = queryLogs(pointStart, pointEnd, request.getVideoStreamId(), request.getRuleId());

                long total = logs.size();
                long success = countSuccess(logs, request.getSuccessType());
                long failure = total - success;

                totalCounts.add(total);
                successCounts.add(success);
                failureCounts.add(failure);
            }
        } else {
            // 7天：每个点代表1天
            for (int i = 0; i < points; i++) {
                LocalDateTime pointEnd = endTime.minusDays(points - 1 - i).withHour(23).withMinute(59).withSecond(59);
                LocalDateTime pointStart = pointEnd.withHour(0).withMinute(0).withSecond(0);

                labels.add(pointEnd.format(DateTimeFormatter.ofPattern("MM-dd")));

                List<DetectionLog> logs = queryLogs(pointStart, pointEnd, request.getVideoStreamId(), request.getRuleId());

                long total = logs.size();
                long success = countSuccess(logs, request.getSuccessType());
                long failure = total - success;

                totalCounts.add(total);
                successCounts.add(success);
                failureCounts.add(failure);
            }
        }

        return TrendDataResponse.builder()
                .labels(labels)
                .totalCounts(totalCounts)
                .successCounts(successCounts)
                .failureCounts(failureCounts)
                .build();
    }

    // 查询日志
    private List<DetectionLog> queryLogs(LocalDateTime startTime, LocalDateTime endTime,
                                         Long videoStreamId, Long ruleId) {
        QueryWrapper wrapper = QueryWrapper.create()
                .where(DETECTION_LOG.CREATED_AT.ge(startTime))
                .and(DETECTION_LOG.CREATED_AT.le(endTime));

        if (videoStreamId != null) {
            wrapper.and(DETECTION_LOG.VIDEO_STREAM_ID.eq(videoStreamId));
        }

        if (ruleId != null) {
            wrapper.and(DETECTION_LOG.AI_RULE_ID.eq(ruleId));
        }

        return detectionLogMapper.selectListByQuery(wrapper);
    }

    // 统计成功次数
    private long countSuccess(List<DetectionLog> logs, String successType) {
        if ("ai_result".equalsIgnoreCase(successType)) {
            // 根据 AI 识别结果中的 success 字段判断
            return logs.stream()
                    .filter(log -> log.getAiSuccess() != null && log.getAiSuccess() == 1)
                    .count();
        } else {
            // 根据执行状态判断
            return logs.stream()
                    .filter(log -> "SUCCESS".equals(log.getStatus()))
                    .count();
        }
    }

    // 获取开始时间
    private LocalDateTime getStartTime(String timeRange) {
        if ("7d".equalsIgnoreCase(timeRange)) {
            return LocalDateTime.now().minusDays(7);
        } else {
            return LocalDateTime.now().minusHours(24);
        }
    }
}
