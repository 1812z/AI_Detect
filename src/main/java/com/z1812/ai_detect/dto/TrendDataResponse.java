package com.z1812.ai_detect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 趋势数据响应

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendDataResponse {
     // 时间标签列表
    private List<String> labels;

     // 总执行次数数据点
    private List<Long> totalCounts;

     // 成功次数数据点
    private List<Long> successCounts;

     // 失败次数数据点
    private List<Long> failureCounts;
}
