package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.DetectionLog;
import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.service.DetectionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class DetectionLogController {

    private final DetectionLogService logService;

    // 获取视频流规则执行日志
    @GetMapping("/{ruleId}")
    public Result<List<DetectionLog>> getLogsByRule(
            @PathVariable Long ruleId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        return Result.success(logService.getLogsByRuleId(ruleId, limit));
    }
}
