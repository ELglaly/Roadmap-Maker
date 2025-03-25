package com.roadmap.backendapi.handler;


import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoadmapWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        session.sendMessage(new TextMessage("Connected to WebSocket."));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus status) throws Exception {
        sessions.remove(session.getId());
    }

    public void sendProgressUpdate(String message) {
        sessions.forEach((id, session) -> {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                System.err.println("Failed to send WebSocket message: " + e.getMessage());
            }
        });
    }
}