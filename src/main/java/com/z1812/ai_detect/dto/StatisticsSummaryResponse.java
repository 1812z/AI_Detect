package com.z1812.ai_detect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统计摘要响应
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSummaryResponse {
    private Long totalCount;
    private Long successCount;
    private Long failureCount;
    private Double successRate;
}
