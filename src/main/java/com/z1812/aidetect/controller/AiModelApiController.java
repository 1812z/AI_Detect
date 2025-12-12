package com.z1812.aidetect.controller;

import com.z1812.aidetect.dto.Result;
import com.z1812.aidetect.entity.AiModelApi;
import com.z1812.aidetect.service.AiModelApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/model")
@RequiredArgsConstructor
public class AiModelApiController {

    private final AiModelApiService service;

    @GetMapping("/list")
    public Result<List<AiModelApi>> list() {
        return Result.success(service.list());
    }

    @GetMapping("/{id}")
    public Result<AiModelApi> getById(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody AiModelApi entity) {
        service.save(entity);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody AiModelApi entity) {
        service.update(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }
}
