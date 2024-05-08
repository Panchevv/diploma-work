package com.diploma.panchev.apigraphql.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class GlobalCorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins("http://localhost:3000") // Allow requests from your frontend
                .allowedMethods("GET, POST, PUT, DELETE, OPTIONS") // Allow common HTTP methods
                .allowedHeaders("Content-Type, Authorization") // Allow necessary headers
                .allowCredentials(true); // Allow cookies and credentials
    }
}
