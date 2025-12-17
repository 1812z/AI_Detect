package com.z1812.ai_detect.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("ai_model_api")
public class AiModelApi {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String name;

    private String baseUrl;

    private String apiKey;

    private String modelName;

    private Integer enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
