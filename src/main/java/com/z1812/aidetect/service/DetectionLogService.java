package com.z1812.aidetect.service;

import com.z1812.aidetect.dto.DetectionLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DetectionLogService {

    private final AtomicLong idGenerator = new AtomicLong(1);

    private final Map<Long, List<DetectionLog>> logsByRule = new ConcurrentHashMap<>();

    private static final int MAX_LOGS_PER_RULE = 100;

    public DetectionLog addLog(Long videoStreamId, String videoStreamName,
                               Long ruleId, String ruleName,
                               String status, String result,
                               String errorMessage, Long duration) {

        DetectionLog log = DetectionLog.builder()
                .id(idGenerator.getAndIncrement())
                .videoStreamId(videoStreamId)
                .videoStreamName(videoStreamName)
                .ruleId(ruleId)
                .ruleName(ruleName)
                .status(status)
                .result(result)
                .errorMessage(errorMessage)
                .createTime(LocalDateTime.now())
                .duration(duration)
                .build();

        logsByRule.computeIfAbsent(ruleId, k -> new CopyOnWriteArrayList<>()).add(0, log);
        if (logsByRule.get(ruleId).size() > MAX_LOGS_PER_RULE) {
            logsByRule.get(ruleId).remove(logsByRule.get(ruleId).size() - 1);
        }

        return log;
    }

    public List<DetectionLog> getLogsByRuleId(Long ruleId, Integer limit) {
        List<DetectionLog> logs = logsByRule.getOrDefault(ruleId, new ArrayList<>());
        if (limit != null && limit > 0) {
            return logs.stream().limit(limit).collect(Collectors.toList());
        }
        return new ArrayList<>(logs);
    }
}
