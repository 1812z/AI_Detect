package com.z1812.ai_detect.dto;

import lombok.Data;

// 统计查询的参数
@Data
public class StatisticsRequest {
    private String timeRange = "24h";      // 时间范围: 24h或7d
    private String successType = "execution";  // 成功类型: execution或ai_result
    private Long videoStreamId;    // 视频流ID(可选)
    private Long ruleId;          // 规则ID(可选)
}
