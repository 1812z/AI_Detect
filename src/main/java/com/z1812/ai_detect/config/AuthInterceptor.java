package com.z1812.ai_detect.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.util.TokenManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final TokenManager tokenManager;
    private final ObjectMapper objectMapper;

    @Value("${auth.token-expire-minutes:0}")
    private int tokenExpireMinutes;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的token
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendUnauthorizedResponse(response, "未提供认证信息");
            return false;
        }

        String token = authHeader.substring(7);

        if (!tokenManager.validateToken(token, tokenExpireMinutes)) {
            sendUnauthorizedResponse(response, "认证信息无效或已过期");
            return false;
        }

        return true;
    }

    private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws Exception {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        Result<Void> result = Result.error(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
