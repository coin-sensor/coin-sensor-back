package com.coinsensor.websocket.service;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WebSocketSessionService {

	private final SimpMessagingTemplate messagingTemplate;
	private final ConcurrentHashMap<String, String> activeSessions = new ConcurrentHashMap<>();

	public void addSession(String sessionId) {
		activeSessions.put(sessionId, sessionId);
		broadcastActiveUserCount();
	}

	public void removeSession(String sessionId) {
		if (activeSessions.remove(sessionId) != null) {
			broadcastActiveUserCount();
		}
	}

	public void broadcastActiveUserCount() {
		int count = getActiveUserCount();
		messagingTemplate.convertAndSend("/topic/activeUsers", count);
	}

	public int getActiveUserCount() {
		return activeSessions.size();
	}
}