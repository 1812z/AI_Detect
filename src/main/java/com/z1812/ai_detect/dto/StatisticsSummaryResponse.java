package com.z1812.ai_detect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// 统计摘要的返回结果（饼图数据）
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsSummaryResponse {
    private Long totalCount;     // 总数
    private Long successCount;   // 成功数
    private Long failureCount;   // 失败数
    private Double successRate;  // 成功率
}
