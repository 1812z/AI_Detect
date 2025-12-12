package com.z1812.aidetect.controller;

import com.z1812.aidetect.dto.DetectionLog;
import com.z1812.aidetect.dto.Result;
import com.z1812.aidetect.service.DetectionLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class DetectionLogController {

    private final DetectionLogService logService;

    @GetMapping("/{ruleId}")
    public Result<List<DetectionLog>> getLogsByRule(
            @PathVariable Long ruleId,
            @RequestParam(required = false, defaultValue = "50") Integer limit) {
        return Result.success(logService.getLogsByRuleId(ruleId, limit));
    }
}
