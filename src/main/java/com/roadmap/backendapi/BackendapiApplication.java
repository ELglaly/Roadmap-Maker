package com.roadmap.backendapi;

import com.roadmap.backendapi.env.EnvLoader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.client.ResourceAccessException;

@SpringBootApplication
@EnableWebSecurity
@EnableCaching
public class BackendapiApplication {
	public static void main(String[] args) {
		EnvLoader.load();
        try {
            SpringApplication.run(BackendapiApplication.class, args);
        } catch (ResourceAccessException e)  {
            throw new RuntimeException(e);
        }

    }

}
