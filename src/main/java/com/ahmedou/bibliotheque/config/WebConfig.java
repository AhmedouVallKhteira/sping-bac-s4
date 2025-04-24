package com.ahmedou.bibliotheque.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @SuppressWarnings("null")
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        
        // auteurs
        registry.addResourceHandler("/uploads/auteurs/**")
                .addResourceLocations("file:" + uploadDir + "/auteurs/");

        // livres
        registry.addResourceHandler("/uploads/livres/**")
                .addResourceLocations("file:" + uploadDir + "/livres/");
    }
}
