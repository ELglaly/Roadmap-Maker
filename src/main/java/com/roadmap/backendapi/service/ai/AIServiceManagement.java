package com.roadmap.backendapi.service.ai;

/**
 * Extended AI service interface for management operations.
 */
public interface AIServiceManagement {

    /**
     * Validates AI provider configuration.
     */
    void validateConfiguration();

    /**
     * Gets current usage metrics.
     *
     * @return the AI provider metrics
     */
    AIProviderMetrics getMetrics();
}
