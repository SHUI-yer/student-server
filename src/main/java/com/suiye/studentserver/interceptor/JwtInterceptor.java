package com.suiye.studentserver.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行 OPTIONS 请求（跨域预检请求，直接放行，否则前端会卡死）
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 2. 从 HTTP 请求头中摸出 Authorization 通行证
        String token = request.getHeader("Authorization");

        // 3. 防呆安全检查：如果没有带 Token，或者格式不对（标准格式是 Bearer xxx）
        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 强行写入 401 状态码
            response.getWriter().write("Unauthorized: Missing or invalid token format.");
            return false; // ❌ 截断请求，拒绝让其进入 Controller！
        }

        // 4. 剥离前缀，提取出真正的密文 Token 字符串
        String actualToken = token.substring(7);

        try {
            // 5. 召唤密钥算法，对 Token 进行强行验签与时效解密
            Algorithm algorithm = Algorithm.HMAC256("my_secret_key_123"); // 必须和登录颁发时的密钥完全一致！
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(actualToken); // 验签核心
            return true; // 🔑 验签成功，开闸放行！
        } catch (JWTVerificationException e) {
            // 6. 验签失败（篡改、过期、伪造）
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 强行写入 401
            response.getWriter().write("Unauthorized: Token has expired or verification failed.");
            return false; // ❌ 截断请求
        }
    }
}
