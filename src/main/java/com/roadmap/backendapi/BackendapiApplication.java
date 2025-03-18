package com.roadmap.backendapi;

import com.roadmap.backendapi.env.EnvLoader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class BackendapiApplication {
	public static void main(String[] args) {
		EnvLoader.load();
		SpringApplication.run(BackendapiApplication.class, args);

	}
	@Bean
	CommandLineRunner commandLineRunner(ChatClient.Builder chatClient) {
		return args -> {
			var chatClient1 = chatClient.defaultSystem("You are a friendly chat bot that answers questions about the weather.").build();
			String response = chatClient1.prompt("Tell me a joke or a story").call().content();
			System.out.println("Hello, Gemini!");
		};
	}
}
