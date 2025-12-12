package com.z1812.aidetect.controller;

import com.z1812.aidetect.dto.LoginRequest;
import com.z1812.aidetect.dto.LoginResponse;
import com.z1812.aidetect.dto.Result;
import com.z1812.aidetect.util.TokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final TokenManager tokenManager;

    @Value("${auth.password}")
    private String configPassword;

    /**
     * 登录接口
     */
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

    /**
     * 登出接口
     */
    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            tokenManager.removeToken(token);
        }
        return Result.success();
    }

    /**
     * 检查登录状态
     */
    @GetMapping("/check")
    public Result<Void> check() {
        return Result.success();
    }
}
