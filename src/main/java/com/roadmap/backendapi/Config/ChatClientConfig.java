package com.roadmap.backendapi.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// add class description for javadoc
/**
 * Configuration class for ChatClient.
 * This class is responsible for creating a ChatClient bean.
 * System prompts are now managed through external prompt templates.
 *
 * @see ChatClient
 * @see org.springframework.ai.chat.client.ChatClient.Builder
 * */

@Configuration
public class ChatClientConfig {

    /**
     * Creates a ChatClient bean.
     * System prompts are provided via PromptService instead of hardcoded defaults.
     *
     * @param builder the ChatClient.Builder used to create the ChatClient
     * @return a ChatClient instance
     */
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }
}
