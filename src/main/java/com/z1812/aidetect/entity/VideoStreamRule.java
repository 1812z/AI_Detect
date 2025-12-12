package com.z1812.aidetect.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("video_stream_rule")
public class VideoStreamRule {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private Long videoStreamId;

    private Long aiRuleId;

    private Integer enabled;

    private LocalDateTime createdAt;
}
