package com.roadmap.backendapi.service.ai.impl;

import com.roadmap.backendapi.exception.ConnectionErrorException;
import com.roadmap.backendapi.service.ai.AIProviderMetrics;
import com.roadmap.backendapi.service.ai.AIProviderService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

/**
 * Gemini AI provider implementation using Spring AI ChatClient.
 * This is the current primary AI provider for the application.
 */
@Service("geminiService")
@ConditionalOnProperty(name = "ai.provider.gemini.enabled", havingValue = "true", matchIfMissing = true)
public class GeminiServiceImpl implements AIProviderService {

    private final ChatClient chatClient;
    private final AIProviderMetrics metrics;

    public GeminiServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.metrics = new AIProviderMetrics("Gemini");
    }

    @Override
    public <T> T generate(String prompt, Class<T> responseType) {
        try {
            metrics.setRequestCount(metrics.getRequestCount() + 1);
            long startTime = System.currentTimeMillis();

            T result = chatClient.prompt(prompt).call().entity(responseType);

            long responseTime = System.currentTimeMillis() - startTime;
            updateAverageResponseTime(responseTime);

            return result;
        } catch (ResourceAccessException e) {
            metrics.setErrorCount(metrics.getErrorCount() + 1);
            throw new ConnectionErrorException();
        }
    }

    @Override
    public <T> T generate(String prompt, ParameterizedTypeReference<T> responseType) {
        try {
            metrics.setRequestCount(metrics.getRequestCount() + 1);
            long startTime = System.currentTimeMillis();

            T result = chatClient.prompt(prompt).call().entity(responseType);

            long responseTime = System.currentTimeMillis() - startTime;
            updateAverageResponseTime(responseTime);

            return result;
        } catch (ResourceAccessException e) {
            metrics.setErrorCount(metrics.getErrorCount() + 1);
            throw new ConnectionErrorException();
        }
    }

    @Override
    public String getProviderName() {
        return "Gemini";
    }

    @Override
    public boolean isAvailable() {
        // Simple health check - can be enhanced with actual API health check
        return true;
    }

    @Override
    public void validateConfiguration() {
        // Validation logic can be added here
        if (chatClient == null) {
            throw new IllegalStateException("ChatClient is not configured");
        }
    }

    @Override
    public AIProviderMetrics getMetrics() {
        return metrics;
    }

    private void updateAverageResponseTime(long newResponseTime) {
        long totalRequests = metrics.getRequestCount();
        long currentAvg = metrics.getAverageResponseTime();

        // Calculate new running average
        long newAvg = ((currentAvg * (totalRequests - 1)) + newResponseTime) / totalRequests;
        metrics.setAverageResponseTime(newAvg);
    }
}
