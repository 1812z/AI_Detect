package com.z1812.ai_detect.controller;

import com.z1812.ai_detect.dto.LoginRequest;
import com.z1812.ai_detect.dto.LoginResponse;
import com.z1812.ai_detect.dto.Result;
import com.z1812.ai_detect.util.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

// 登录登出控制器

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenManager tokenManager;

    @Value("${auth.password}")
    private String configPassword;

     // 登录接口
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return Result.error(400, "密码不能为空");
        }

        if (!configPassword.equals(request.getPassword())) {
            return Result.error(401, "密码错误");
        }

        String token = tokenManager.generateToken();
        return Result.success(new LoginResponse(token));
    }

    // 登出接口
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenManager.removeToken(token);
        }
        return Result.success();
    }

     // 检查登录状态，由请求拦截器控制
    @GetMapping("/check")
    public Result<Void> check() {
        return Result.success();
    }
}
