package com.z1812.ai_detect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 趋势数据的返回结果（折线图数据）
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataResponse {
    private List<String> labels;        // 时间标签(横坐标)
    private List<Long> totalCounts;     // 总数数据点
    private List<Long> successCounts;   // 成功数据点
    private List<Long> failureCounts;   // 失败数据点
}
