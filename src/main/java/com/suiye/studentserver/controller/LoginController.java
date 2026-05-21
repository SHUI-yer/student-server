package com.suiye.studentserver.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.suiye.studentserver.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class LoginController {

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody Map<String, String> user) {
        String username = user.get("username");
        String password = user.get("password");

        // 这里先用在 DataGrip 里插入的初始管理员数据做比对
        if ("admin".equals(username) && "123456".equals(password)) {
            // 登录成功，生成一个 1 天后过期的 JWT Token
            String token = JWT.create()
                    .withClaim("username", username)
                    .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                    .sign(Algorithm.HMAC256("my_secret_key_123")); // 加密密钥

            Map<String, String> data = new HashMap<>();
            data.put("token", token);
            return Result.success(data);
        } else {
            return Result.error(400, "用户名或密码错误");
        }
    }
}