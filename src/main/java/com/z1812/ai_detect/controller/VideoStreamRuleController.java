package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.entity.VideoStreamRule;
import com.z1812.ai_detect.service.VideoStreamRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stream-rule")
@RequiredArgsConstructor
public class VideoStreamRuleController {

    private final VideoStreamRuleService service;

    @GetMapping("/list")
    public Result<List<VideoStreamRule>> list() {
        return Result.success(service.list());
    }

    @GetMapping("/{id}")
    public Result<VideoStreamRule> getById(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody VideoStreamRule entity) {
        service.save(entity);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody VideoStreamRule entity) {
        service.update(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }

    @GetMapping("/by-stream/{streamId}")
    public Result<List<VideoStreamRule>> listByStreamId(@PathVariable Long streamId) {
        return Result.success(service.listByVideoStreamId(streamId));
    }
}
