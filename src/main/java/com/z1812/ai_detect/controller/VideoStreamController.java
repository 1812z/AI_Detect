package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.entity.VideoStream;
import com.z1812.ai_detect.service.VideoStreamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stream")
@RequiredArgsConstructor
public class VideoStreamController {

    private final VideoStreamService service;

    @GetMapping("/list")
    public Result<List<VideoStream>> list() {
        return Result.success(service.list());
    }

    @GetMapping("/{id}")
    public Result<VideoStream> getById(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody VideoStream entity) {
        service.save(entity);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody VideoStream entity) {
        service.update(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }
}
