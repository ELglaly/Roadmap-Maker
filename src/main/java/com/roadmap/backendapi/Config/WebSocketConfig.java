package com.roadmap.backendapi.Config;

import com.roadmap.backendapi.handler.RoadmapWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final RoadmapWebSocketHandler roadmapWebSocketHandler;

    public WebSocketConfig(RoadmapWebSocketHandler roadmapWebSocketHandler)
    {
        this.roadmapWebSocketHandler = roadmapWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(roadmapWebSocketHandler,"api/v1/roadmaps/create/")
                .setAllowedOrigins("*");

    }
}
