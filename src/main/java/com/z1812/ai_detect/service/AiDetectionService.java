package com.z1812.ai_detect.service;

import com.z1812.ai_detect.entity.AiModelApi;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.util.OpenAIClient;
import com.z1812.ai_detect.util.VideoCapture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiDetectionService {

    private final VideoCapture videoCapture;
    private final OpenAIClient openAIClient;
    private final AiModelApiService modelApiService;

    public String detectByRule(String streamUrl, AiRule rule) {
        try {
            byte[] imageBytes = videoCapture.captureFrame(streamUrl);

            AiModelApi modelApi = modelApiService.getById(rule.getModelApiId());
            if (modelApi == null || modelApi.getEnabled() != 1) {
                throw new RuntimeException("模型API未找到或未启用");
            }

            String prompt = rule.getPromptTemplate();

            String result = openAIClient.analyzeImage(
                    modelApi.getBaseUrl(),
                    modelApi.getApiKey(),
                    modelApi.getModelName(),
                    prompt,
                    imageBytes
            );

            log.info("AI检测完成 - 规则: {}, 结果: {}", rule.getName(), result);
            return result;

        } catch (Exception e) {
            log.error("AI检测失败 - 规则: {}", rule.getName(), e);
            throw new RuntimeException("AI检测失败: " + e.getMessage(), e);
        }
    }
}
