package com.roadmap.backendapi.Config;

import com.roadmap.backendapi.handler.RoadmapWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration class for WebSocket.
 * This class is responsible for configuring WebSocket handlers and their endpoints.
 * It registers the RoadmapWebSocketHandler to handle WebSocket connections at the specified endpoint.
 *
 * @see WebSocketConfigurer
 * @see RoadmapWebSocketHandler
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final RoadmapWebSocketHandler roadmapWebSocketHandler;

    /**
     * Constructor for WebSocketConfig.
     *
     * @param roadmapWebSocketHandler the RoadmapWebSocketHandler to be registered
     */
    public WebSocketConfig(RoadmapWebSocketHandler roadmapWebSocketHandler)
    {
        this.roadmapWebSocketHandler = roadmapWebSocketHandler;
    }

    /**
     * Registers WebSocket handlers and their endpoints.
     *
     * @param registry the WebSocketHandlerRegistry used to register the handlers
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(roadmapWebSocketHandler,"api/v1/roadmaps/create/")
                .setAllowedOrigins("*");

    }
}
