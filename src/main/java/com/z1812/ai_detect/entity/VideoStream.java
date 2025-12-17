package com.z1812.ai_detect.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("video_stream")
public class VideoStream {

    @Id(keyType = KeyType.Auto)
    private Long id;

    private String name;

    private String streamUrl;

    private Integer enabled;

    private Integer intervalSeconds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
