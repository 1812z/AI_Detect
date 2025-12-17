package com.z1812.ai_detect.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenManager {

    private final Map<String, LocalDateTime> tokenStore = new ConcurrentHashMap<>();

    /**
     * 生成token
     */
    public String generateToken() {
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, LocalDateTime.now());
        return token;
    }

    /**
     * 验证token是否有效
     */
    public boolean validateToken(String token, int expireMinutes) {
        if (token == null || !tokenStore.containsKey(token)) {
            return false;
        }

        // 如果设置为0，表示永不过期
        if (expireMinutes == 0) {
            return true;
        }

        LocalDateTime createTime = tokenStore.get(token);
        LocalDateTime now = LocalDateTime.now();
        return createTime.plusMinutes(expireMinutes).isAfter(now);
    }

    /**
     * 删除token（登出）
     */
    public void removeToken(String token) {
        if (token != null) {
            tokenStore.remove(token);
        }
    }

    /**
     * 清理过期的token
     */
    public void cleanExpiredTokens(int expireMinutes) {
        if (expireMinutes == 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        tokenStore.entrySet().removeIf(entry ->
            entry.getValue().plusMinutes(expireMinutes).isBefore(now)
        );
    }
}
