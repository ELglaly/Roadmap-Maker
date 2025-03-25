package com.roadmap.backendapi;

import com.roadmap.backendapi.env.EnvLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@SpringBootApplication
@EnableWebSecurity
public class BackendapiApplication {
	public static void main(String[] args) {
		EnvLoader.load();
        try {
            SpringApplication.run(BackendapiApplication.class, args);
        } catch (ResourceAccessException e)  {
            throw new RuntimeException(e);
        }

    }

//	@Bean
//	CommandLineRunner commandLineRunner(ChatClient.Builder chatClient) {
//		return args -> {
//			var chatClient1 = chatClient.defaultSystem("You are a friendly chat bot that answers questions about the weather.").build();
//			String response = chatClient1.prompt("Tell me a joke or a story").call().content();
//			System.out.println("Hello, Gemini!");
//		};
//	}


}
