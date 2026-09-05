package com.roadmap.backendapi.Config;

import com.roadmap.backendapi.service.ai.AIProviderService;
import com.roadmap.backendapi.service.ai.AIServiceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Configuration class for AI services.
 * Configures the primary AI provider and fallback strategy.
 */
@Configuration
public class AIServiceConfig {

    @Value("${ai.provider.primary:gemini}")
    private String primaryProvider;

    @Value("${ai.provider.fallback.enabled:false}")
    private boolean fallbackEnabled;

    /**
     * Primary AI service bean based on configuration.
     * Uses fallback service if enabled, otherwise uses configured primary provider.
     *
     * @param factory the AI service factory
     * @return the primary AI provider service
     */
    @Bean
    @Primary
    public AIProviderService primaryAIService(AIServiceFactory factory) {
        if (fallbackEnabled) {
            return factory.getProvider("fallback");
        }
        return factory.getProvider(primaryProvider);
    }
}
