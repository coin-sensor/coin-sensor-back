package com.coinsensor.websocket.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.coinsensor.websocket.service.WebSocketSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

	private final WebSocketSessionService sessionService;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();

		sessionService.addSession(sessionId);
		log.info("WebSocket connection established - SessionId: {}, Active users: {}", sessionId,
			sessionService.getActiveUserCount());
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		String sessionId = headerAccessor.getSessionId();

		sessionService.removeSession(sessionId);
		log.info("WebSocket connection closed - SessionId: {}, Active users: {}", sessionId,
			sessionService.getActiveUserCount());
	}
}