package com.roadmap.backendapi.service.ai.impl;

import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.service.ai.AIProviderMetrics;
import com.roadmap.backendapi.service.ai.AIProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Fallback AI service implementation using decorator pattern.
 * Cascades through multiple providers when primary provider fails.
 */
@Service("fallbackAIService")
@ConditionalOnProperty(name = "ai.provider.fallback.enabled", havingValue = "true", matchIfMissing = false)
public class FallbackAIServiceImpl implements AIProviderService {

    private final List<AIProviderService> providers;
    private final AIProviderMetrics metrics;

    public FallbackAIServiceImpl(List<AIProviderService> providers) {
        this.providers = providers;
        this.metrics = new AIProviderMetrics("FallbackAI");
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        ConnectionErrorException lastException = null;

        for (AIProviderService provider : providers) {
            // Skip fallback service itself to avoid recursion
            if (provider instanceof FallbackAIServiceImpl) {
                continue;
            }

            if (!provider.isAvailable()) {
                continue;
            }

            try {
                metrics.setRequestCount(metrics.getRequestCount() + 1);
                return provider.generate(prompt, responseType);
            } catch (ConnectionErrorException e) {
                metrics.setErrorCount(metrics.getErrorCount() + 1);
                lastException = e;
                // Try next provider
            }
        }

        throw lastException != null ? lastException : new ConnectionErrorException();
    }

    @Override
    public <T> T generate(String prompt, ParameterizedTypeReference<T> responseType) {
        ConnectionErrorException lastException = null;

        for (AIProviderService provider : providers) {
            // Skip fallback service itself to avoid recursion
            if (provider instanceof FallbackAIServiceImpl) {
                continue;
            }

            if (!provider.isAvailable()) {
                continue;
            }

            try {
                metrics.setRequestCount(metrics.getRequestCount() + 1);
                return provider.generate(prompt, responseType);
            } catch (ConnectionErrorException e) {
                metrics.setErrorCount(metrics.getErrorCount() + 1);
                lastException = e;
            }
        }

        throw lastException != null ? lastException : new ConnectionErrorException();
    }

    @Override
    public String getProviderName() {
        return "FallbackAI";
    }

    @Override
    public boolean isAvailable() {
        return providers.stream()
                .filter(p -> !(p instanceof FallbackAIServiceImpl))
                .anyMatch(AIProviderService::isAvailable);
    }

    @Override
    public void validateConfiguration() {
        providers.forEach(AIProviderService::validateConfiguration);
    }

    @Override
    public AIProviderMetrics getMetrics() {
        return metrics;
    }
}
