package com.cafe.QR.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * This is the crucial configuration that allows your React frontend
     * (running on localhost:5173) to communicate with your Spring Boot backend.
     * In production, you would change "http://localhost:5173" to your actual
     * frontend domain (e.g., "https://www.sh-cafe.com").
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // Apply CORS to all API endpoints
            .allowedOrigins("http://localhost:5173") // The origin of your Vite React app
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
            .allowedHeaders("*") // Allow all headers
            .allowCredentials(true); // Allow credentials (like cookies or auth headers)
    }
}