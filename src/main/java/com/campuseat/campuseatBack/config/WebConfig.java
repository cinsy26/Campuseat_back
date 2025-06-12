package com.campuseat.campuseatBack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:3000",
                        "http://localhost:8081",
                        "http://10.0.2.2:8080",         // Android Emulator
                        "http://192.168.0.XXX:8080",    // 실제 기기에서 테스트할 경우 로컬 IP
                        "https://your-frontend-domain.vercel.app"  // Vercel 등
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
