package com.roadmap.backendapi.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for Spring MVC.
 * This class is responsible for configuring Spring MVC components.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // The ResponseCacheInterceptor is automatically registered as a ResponseBodyAdvice
    // through the @ControllerAdvice annotation, so we don't need to register it here.
}