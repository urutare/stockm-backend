package com.urutare.syncservice.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class EventWebSocketHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Handle incoming text messages from clients
        // Implement your synchronization logic, OT operations, etc.
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Perform actions when a WebSocket connection is established
        // For example, register the session for broadcasting updates
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Perform actions when a WebSocket connection is closed
        // For example, unregister the session from broadcasting updates
    }
}
