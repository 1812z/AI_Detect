package com.z1812.aidetect.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("ai_rule")
public class AiRule {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long modelApiId;

    private String name;

    private String description;

    private String promptTemplate;

    private Integer enabled;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
