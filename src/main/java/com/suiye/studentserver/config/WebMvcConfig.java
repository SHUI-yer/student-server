package com.suiye.studentserver.config;

import com.suiye.studentserver.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**") // 🛡️ 默认拉响全盘警报：拦截后端所有的接口请求！
                .excludePathPatterns("/api/login", "/uploads/**", "/api/excel/export/**", "/api/excel/template"); // 🔓 放行登录、静态资源、以及 Excel 导出下载接口
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源映射：将 /uploads/** 的请求映射到本地项目根目录下的 uploads 文件夹
        String path = System.getProperty("user.dir") + "/uploads/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + path);
    }
}
