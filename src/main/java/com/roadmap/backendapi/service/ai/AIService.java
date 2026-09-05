package com.roadmap.backendapi.service.ai;

import com.roadmap.backendapi.exception.ConnectionErrorException;
import org.springframework.core.ParameterizedTypeReference;

/**
 * AI Service abstraction for different LLM providers.
 * Provides unified interface for AI operations across OpenAI, Claude, Gemini, and local LLMs.
 */
public interface AIService {

    /**
     * Generates a response from the AI model for the given prompt.
     *
     * @param prompt the input prompt
     * @param responseType the expected response class type
     * @param <T> the type of the response
     * @return the generated response object
     * @throws ConnectionErrorException if connection to AI provider fails
     */
    <T> T generate(String prompt, Class<T> responseType);

    /**
     * Generates a response from the AI model for the given prompt.
     *
     * @param prompt the input prompt
     * @param responseType the parameterized type reference for complex types
     * @param <T> the type of the response
     * @return the generated response object
     * @throws ConnectionErrorException if connection to AI provider fails
     */
    <T> T generate(String prompt, ParameterizedTypeReference<T> responseType);

    /**
     * Returns the provider name (OpenAI, Claude, Gemini, LocalLLM).
     *
     * @return the provider name
     */
    String getProviderName();

    /**
     * Checks if the provider is available/healthy.
     *
     * @return true if provider is available, false otherwise
     */
    boolean isAvailable();
}
