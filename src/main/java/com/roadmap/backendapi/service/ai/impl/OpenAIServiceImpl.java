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
 * OpenAI provider implementation using Spring AI ChatClient.
 * This can be enabled as an alternative or fallback provider.
 */
@Service("openAIService")
@ConditionalOnProperty(name = "ai.provider.openai.enabled", havingValue = "true", matchIfMissing = false)
public class OpenAIServiceImpl implements AIProviderService {

    private final ChatClient chatClient;
    private final AIProviderMetrics metrics;

    public OpenAIServiceImpl(ChatClient chatClient) {
        this.chatClient = chatClient;
        this.metrics = new AIProviderMetrics("OpenAI");
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
        return "OpenAI";
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public void validateConfiguration() {
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
        long newAvg = ((currentAvg * (totalRequests - 1)) + newResponseTime) / totalRequests;
        metrics.setAverageResponseTime(newAvg);
    }
}
