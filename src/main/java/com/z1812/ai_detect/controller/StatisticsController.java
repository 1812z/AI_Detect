package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.dto.StatisticsRequest;
import com.z1812.ai_detect.dto.StatisticsSummaryResponse;
import com.z1812.ai_detect.dto.TrendDataResponse;
import com.z1812.ai_detect.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 统计功能控制器
 * 提供饼图和折线图的数据接口
 */
@Slf4j
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    // 统计摘要，给前端饼图用
    @GetMapping("/summary")
    public Result<StatisticsSummaryResponse> getSummary(
            @RequestParam(defaultValue = "24h") String timeRange,
            @RequestParam(defaultValue = "execution") String successType,
            @RequestParam(required = false) Long videoStreamId,
            @RequestParam(required = false) Long ruleId) {

        StatisticsRequest req = new StatisticsRequest();
        req.setTimeRange(timeRange);
        req.setSuccessType(successType);
        req.setVideoStreamId(videoStreamId);
        req.setRuleId(ruleId);

        log.info("查询统计摘要: {}", timeRange);
        return Result.success(statisticsService.getSummary(req));
    }

    // 趋势数据，给前端折线图用
    @GetMapping("/trend")
    public Result<TrendDataResponse> getTrendData(
            @RequestParam(defaultValue = "24h") String timeRange,
            @RequestParam(defaultValue = "execution") String successType,
            @RequestParam(required = false) Long videoStreamId,
            @RequestParam(required = false) Long ruleId) {

        StatisticsRequest req = new StatisticsRequest();
        req.setTimeRange(timeRange);
        req.setSuccessType(successType);
        req.setVideoStreamId(videoStreamId);
        req.setRuleId(ruleId);

        log.info("查询趋势数据: {}", timeRange);
        return Result.success(statisticsService.getTrendData(req));
    }
}
