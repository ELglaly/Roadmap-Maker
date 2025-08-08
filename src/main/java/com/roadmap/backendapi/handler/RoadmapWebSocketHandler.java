package com.roadmap.backendapi.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket handler for managing connections and sending messages.
 * This class handles WebSocket connections and allows sending messages to connected clients.
 * It maintains a map of active WebSocket sessions.
 */
@Component
public class RoadmapWebSocketHandler extends TextWebSocketHandler {

    public static final Logger logger = LoggerFactory.getLogger(RoadmapWebSocketHandler.class);
    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        session.sendMessage(new TextMessage("Connected to WebSocket."));
        logger.info("WebSocket connection established for session: {}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        logger.info("WebSocket connection closed for session: {} with status: {}", session.getId(), status);
    }

    public void sendProgressUpdate(String message) {
        sessions.forEach((id, session) -> {
            try {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(message));
                    logger.debug("Sent WebSocket message to session {}: {}", id, message);
                } else {
                    logger.warn("Attempted to send message to closed session: {}", id);
                }
            } catch (IOException e) {
                logger.error("Failed to send WebSocket message to session {}: {}", id, e.getMessage(), e);
            }
        });
    }
}