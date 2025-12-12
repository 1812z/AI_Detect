package com.z1812.aidetect.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetectionLog {

    private Long id;

    private Long videoStreamId;

    private String videoStreamName;

    private Long ruleId;

    private String ruleName;

    private String status;

    private String result;

    private String errorMessage;

    private LocalDateTime createTime;

    private Long duration;
}
