package com.watad.youth;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve files from ./uploads/profile_pic as /images/**
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:./uploads/profile_pic/");
    }
}
