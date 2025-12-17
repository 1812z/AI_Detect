package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.entity.VideoStream;
import com.z1812.ai_detect.service.AiDetectionService;
import com.z1812.ai_detect.service.VideoStreamService;
import com.z1812.ai_detect.util.VideoCapture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

// 测试接口功能控制器

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final VideoCapture videoCapture;
    private final VideoStreamService videoStreamService;
    private final AiDetectionService aiDetectionService;

    //
    @GetMapping("/capture/{streamId}")
    public Result<Map<String, Object>> testCapture(@PathVariable Long streamId) {
        try {
            VideoStream stream = videoStreamService.getById(streamId);
            if (stream == null) {
                return Result.error("视频流不存在");
            }

            log.info("测试捕获视频流: {}", stream.getStreamUrl());
            byte[] imageBytes = videoCapture.captureFrame(stream.getStreamUrl());

            Map<String, Object> response = new HashMap<>();
            response.put("streamName", stream.getName());
            response.put("streamUrl", stream.getStreamUrl());
            response.put("imageSize", imageBytes.length);
            response.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
            response.put("message", "成功捕获图像");

            return Result.success(response);
        } catch (Exception e) {
            log.error("测试捕获失败", e);
            return Result.error("捕获失败: " + e.getMessage());
        }
    }

    @GetMapping("/detect/{streamId}/{ruleId}")
    public Result<String> testDetect(@PathVariable Long streamId, @PathVariable Long ruleId) {
        try {
            VideoStream stream = videoStreamService.getById(streamId);
            if (stream == null) {
                return Result.error("视频流不存在");
            }

            AiRule rule = new AiRule();
            rule.setId(ruleId);

            log.info("测试AI识别 - 视频流: {}, 规则ID: {}", stream.getName(), ruleId);
            String result = aiDetectionService.detectByRule(stream.getStreamUrl(), rule);

            return Result.success(result);
        } catch (Exception e) {
            log.error("测试识别失败", e);
            return Result.error("识别失败: " + e.getMessage());
        }
    }

    @PostMapping("/capture-url")
    public Result<Map<String, Object>> testCaptureByUrl(@RequestBody Map<String, String> params) {
        try {
            String url = params.get("url");
            if (url == null || url.isEmpty()) {
                return Result.error("URL 不能为空");
            }

            log.info("测试捕获URL: {}", url);
            byte[] imageBytes = videoCapture.captureFrame(url);

            Map<String, Object> response = new HashMap<>();
            response.put("url", url);
            response.put("imageSize", imageBytes.length);
            response.put("imageBase64", Base64.getEncoder().encodeToString(imageBytes));
            response.put("message", "成功捕获图像");

            return Result.success(response);
        } catch (Exception e) {
            log.error("测试捕获失败", e);
            return Result.error("捕获失败: " + e.getMessage());
        }
    }
}
