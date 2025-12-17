package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.dto.StatisticsRequest;
import com.z1812.ai_detect.dto.StatisticsSummaryResponse;
import com.z1812.ai_detect.dto.TrendDataResponse;
import com.z1812.ai_detect.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

// 统计接口控制器

@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    /**
     * 获取统计摘要（饼图数据）
     *
     * @param timeRange    时间范围：24h 或 7d，默认 24h
     * @param successType  成功类型：execution 或 ai_result，默认 execution
     * @param videoStreamId 视频流ID（可选）
     * @param ruleId       规则ID（可选）
     * @return 统计摘要
     */
    @GetMapping("/summary")
    public Result<StatisticsSummaryResponse> getSummary(
            @RequestParam(defaultValue = "24h") String timeRange,
            @RequestParam(defaultValue = "execution") String successType,
            @RequestParam(required = false) Long videoStreamId,
            @RequestParam(required = false) Long ruleId) {

        StatisticsRequest request = new StatisticsRequest();
        request.setTimeRange(timeRange);
        request.setSuccessType(successType);
        request.setVideoStreamId(videoStreamId);
        request.setRuleId(ruleId);

        log.info("获取统计摘要 - 时间范围: {}, 成功类型: {}, 视频流ID: {}, 规则ID: {}",
                timeRange, successType, videoStreamId, ruleId);

        StatisticsSummaryResponse summary = statisticsService.getSummary(request);
        return Result.success(summary);
    }

    /**
     * 获取趋势数据（折线图数据）
     *
     * @param timeRange    时间范围：24h 或 7d，默认 24h
     * @param successType  成功类型：execution 或 ai_result，默认 execution
     * @param videoStreamId 视频流ID（可选）
     * @param ruleId       规则ID（可选）
     * @return 趋势数据
     */
    @GetMapping("/trend")
    public Result<TrendDataResponse> getTrendData(
            @RequestParam(defaultValue = "24h") String timeRange,
            @RequestParam(defaultValue = "execution") String successType,
            @RequestParam(required = false) Long videoStreamId,
            @RequestParam(required = false) Long ruleId) {

        StatisticsRequest request = new StatisticsRequest();
        request.setTimeRange(timeRange);
        request.setSuccessType(successType);
        request.setVideoStreamId(videoStreamId);
        request.setRuleId(ruleId);

        log.info("获取趋势数据 - 时间范围: {}, 成功类型: {}, 视频流ID: {}, 规则ID: {}",
                timeRange, successType, videoStreamId, ruleId);

        TrendDataResponse trendData = statisticsService.getTrendData(request);
        return Result.success(trendData);
    }
}
