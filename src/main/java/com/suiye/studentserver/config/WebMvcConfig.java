package com.suiye.studentserver.config;

import com.suiye.studentserver.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 🛡️ 默认拉响全盘警报：拦截后端所有的接口请求！
                .excludePathPatterns("/api/login"); // 🔓 唯独放行登录接口，不然谁也登不进来！
    }
}
