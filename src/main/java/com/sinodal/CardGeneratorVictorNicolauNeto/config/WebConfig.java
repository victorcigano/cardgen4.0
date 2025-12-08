package com.sinodal.CardGeneratorVictorNicolauNeto.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/cards/**")
                .allowedOrigins("http://localhost:8080", "https://localhost:8080")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "X-CG-USER")
                .allowCredentials(true)
                .maxAge(3600);
        
        registry.addMapping("/admin/**")
                .allowedOrigins("http://localhost:8080", "https://localhost:8080")
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("Content-Type", "X-CG-USER", "X-CG-ADMIN")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
