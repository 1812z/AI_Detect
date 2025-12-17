package com.z1812.ai_detect.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("detection_log")
public class DetectionLog {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long videoStreamId;

    private Long aiRuleId;

    private Long videoStreamRuleId;

    private String status;

    private String aiResult;

    private Integer aiSuccess;

    private String errorMessage;

    private Integer executionTime;

    private LocalDateTime createdAt;
}
