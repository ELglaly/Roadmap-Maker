package com.roadmap.backendapi.Config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// add class description for javadoc
/**
 * Configuration class for ChatClient.
 * This class is responsible for creating a ChatClient bean with a default system prompt.
 * The default system prompt is set to "You are a friendly chat bot that answers questions about the weather."
 * This configuration is used to initialize the ChatClient with the specified prompt.
 *
 * @see ChatClient
 * @see org.springframework.ai.chat.client.ChatClient.Builder
 * */

@Configuration
public class ChatClientConfig {

    /**
     * Creates a ChatClient bean with a default system prompt.
     *
     * @param builder the ChatClient.Builder used to create the ChatClient
     * @return a ChatClient instance with the specified default system prompt
     */
    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem("You are a friendly chat bot that answers questions about the weather.").build();

    }
}
