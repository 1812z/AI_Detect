package com.z1812.ai_detect.dto;

import lombok.Data;

/**
 * 统计请求参数
 */
@Data
public class StatisticsRequest {

    /**
     * 时间范围：24h（24小时）或 7d（7天）
     * 默认：24h
     */
    private String timeRange = "24h";

    /**
     * 成功类型：execution（执行成功）或 ai_result（AI识别结果中的success字段）
     * 默认：execution
     */
    private String successType = "execution";

    /**
     * 视频流ID（可选，不传则统计所有视频流）
     */
    private Long videoStreamId;

    /**
     * 规则ID（可选，不传则统计所有规则）
     */
    private Long ruleId;
}
