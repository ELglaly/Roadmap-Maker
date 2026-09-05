package com.roadmap.backendapi.service.ai;

import lombok.Data;

/**
 * Metrics data class for AI provider performance tracking.
 */
@Data
public class AIProviderMetrics {
    private final String providerName;
    private long requestCount;
    private long errorCount;
    private long averageResponseTime;

    /**
     * Constructor for AIProviderMetrics.
     *
     * @param providerName the name of the AI provider
     */
    public AIProviderMetrics(String providerName) {
        this.providerName = providerName;
        this.requestCount = 0;
        this.errorCount = 0;
        this.averageResponseTime = 0;
    }
}
