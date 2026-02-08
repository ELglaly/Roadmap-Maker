package com.roadmap.backendapi.service.ai;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Factory for selecting and creating AI provider services.
 * Provides centralized provider selection based on configuration.
 */
@Component
public class AIServiceFactory {

    private final Map<String, AIProviderService> aiServices;

    public AIServiceFactory(Map<String, AIProviderService> aiServices) {
        this.aiServices = aiServices;
    }

    /**
     * Gets an AI provider by name.
     *
     * @param providerName the provider name (e.g., "gemini", "openai", "mock")
     * @return the AI provider service
     * @throws IllegalArgumentException if provider is not found
     */
    public AIProviderService getProvider(String providerName) {
        AIProviderService service = aiServices.get(providerName + "Service");
        if (service == null) {
            throw new IllegalArgumentException("Unknown AI provider: " + providerName);
        }
        return service;
    }

    /**
     * Gets the primary provider (first available provider).
     *
     * @return the primary AI provider service
     * @throws IllegalStateException if no provider is available
     */
    public AIProviderService getPrimaryProvider() {
        return aiServices.values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No AI provider available"));
    }
}
