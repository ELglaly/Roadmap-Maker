package com.roadmap.backendapi.service.ai.impl;

import com.roadmap.backendapi.service.ai.AIProviderMetrics;
import com.roadmap.backendapi.service.ai.AIProviderService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Mock AI service implementation for testing.
 * Returns empty objects to avoid real AI API calls during tests.
 */
@Service("mockAIService")
@ConditionalOnProperty(name = "ai.provider.mock.enabled", havingValue = "true", matchIfMissing = false)
public class MockAIServiceImpl implements AIProviderService {

    private final AIProviderMetrics metrics;

    public MockAIServiceImpl() {
        this.metrics = new AIProviderMetrics("Mock");
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        metrics.setRequestCount(metrics.getRequestCount() + 1);

        // Return mock objects for testing
        try {
            return responseType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T generate(String prompt, ParameterizedTypeReference<T> responseType) {
        metrics.setRequestCount(metrics.getRequestCount() + 1);

        // Return empty list for parameterized types (typically List<T>)
        return (T) new ArrayList<>();
    }

    @Override
    public String getProviderName() {
        return "Mock";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void validateConfiguration() {
        // No validation needed for mock
    }

    @Override
    public AIProviderMetrics getMetrics() {
        return metrics;
    }
}
