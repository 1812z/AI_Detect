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

/**
 * 统计服务
 * 用来统计检测日志的成功率和趋势
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final DetectionLogMapper logMapper;

    // 获取摘要数据（总数、成功数、失败数、成功率）
    public StatisticsSummaryResponse getSummary(StatisticsRequest req) {
        LocalDateTime start = calcStartTime(req.getTimeRange());
        LocalDateTime end = LocalDateTime.now();

        // 查询这段时间的所有日志
        List<DetectionLog> logs = getLogs(start, end, req.getVideoStreamId(), req.getRuleId());

        long total = logs.size();
        long success = calcSuccessCount(logs, req.getSuccessType());
        long fail = total - success;

        // 算成功率，保留两位小数
        double rate = total > 0 ? (success * 100.0 / total) : 0;
        rate = Math.round(rate * 100.0) / 100.0;

        return StatisticsSummaryResponse.builder()
                .totalCount(total)
                .successCount(success)
                .failureCount(fail)
                .successRate(rate)
                .build();
    }

    // 获取趋势数据（折线图用）
    public TrendDataResponse getTrendData(StatisticsRequest req) {
        String range = req.getTimeRange();
        boolean is24h = "24h".equalsIgnoreCase(range);

        LocalDateTime now = LocalDateTime.now();
        int pointCount = is24h ? 24 : 7;  // 24小时或7天

        List<String> labels = new ArrayList<>();
        List<Long> totals = new ArrayList<>();
        List<Long> successes = new ArrayList<>();
        List<Long> fails = new ArrayList<>();

        // 按小时或天分段统计
        for (int i = 0; i < pointCount; i++) {
            LocalDateTime pEnd, pStart;

            if (is24h) {
                // 24小时模式：每个点是1小时
                pEnd = now.minusHours(pointCount - 1 - i);
                pStart = pEnd.minusHours(1);
                labels.add(pEnd.format(DateTimeFormatter.ofPattern("HH:00")));
            } else {
                // 7天模式：每个点是1天
                pEnd = now.minusDays(pointCount - 1 - i).withHour(23).withMinute(59).withSecond(59);
                pStart = pEnd.withHour(0).withMinute(0).withSecond(0);
                labels.add(pEnd.format(DateTimeFormatter.ofPattern("MM-dd")));
            }

            List<DetectionLog> logs = getLogs(pStart, pEnd, req.getVideoStreamId(), req.getRuleId());

            long t = logs.size();
            long s = calcSuccessCount(logs, req.getSuccessType());

            totals.add(t);
            successes.add(s);
            fails.add(t - s);
        }

        return TrendDataResponse.builder()
                .labels(labels)
                .totalCounts(totals)
                .successCounts(successes)
                .failureCounts(fails)
                .build();
    }

    // 从数据库查询日志
    private List<DetectionLog> getLogs(LocalDateTime start, LocalDateTime end,
                                       Long streamId, Long ruleId) {
        QueryWrapper qw = QueryWrapper.create()
                .where(DETECTION_LOG.CREATED_AT.ge(start))
                .and(DETECTION_LOG.CREATED_AT.le(end));

        if (streamId != null) {
            qw.and(DETECTION_LOG.VIDEO_STREAM_ID.eq(streamId));
        }
        if (ruleId != null) {
            qw.and(DETECTION_LOG.AI_RULE_ID.eq(ruleId));
        }

        return logMapper.selectListByQuery(qw);
    }

    // 计算成功的数量
    private long calcSuccessCount(List<DetectionLog> logs, String type) {
        if ("ai_result".equalsIgnoreCase(type)) {
            // 看AI返回的success字段
            return logs.stream()
                    .filter(log -> log.getAiSuccess() != null && log.getAiSuccess() == 1)
                    .count();
        } else {
            // 看执行状态
            return logs.stream()
                    .filter(log -> "SUCCESS".equals(log.getStatus()))
                    .count();
        }
    }

    // 根据时间范围算起始时间
    private LocalDateTime calcStartTime(String range) {
        if ("7d".equalsIgnoreCase(range)) {
            return LocalDateTime.now().minusDays(7);
        }
        return LocalDateTime.now().minusHours(24);
    }
}
