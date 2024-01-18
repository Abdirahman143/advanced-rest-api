package com.example.advancedrestapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .mediaType("v1", MediaType.valueOf("application/vnd.cbc.app-v1+json"))
                .mediaType("v2", MediaType.valueOf("application/vnd.cbc.app-v2+json"));
    }
}