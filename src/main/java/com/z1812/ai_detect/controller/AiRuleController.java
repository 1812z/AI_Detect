package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.service.AiRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// AI规则管理控制器

@RestController
@RequestMapping("/api/rule")
@RequiredArgsConstructor
public class AiRuleController {

    private final AiRuleService service;

    @GetMapping("/list")
    public Result<List<AiRule>> list() {
        return Result.success(service.list());
    }

    @GetMapping("/{id}")
    public Result<AiRule> getById(@PathVariable Long id) {
        return Result.success(service.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody AiRule entity) {
        service.save(entity);
        return Result.success();
    }

    @PutMapping
    public Result<Void> update(@RequestBody AiRule entity) {
        service.update(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }
}
