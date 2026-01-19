package com.coinsensor.websocket.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketSyncService {

	private final RedisTemplate<String, Object> redisTemplate;
	private final SimpMessagingTemplate messagingTemplate;

	public void broadcastSessionConnect(String sessionId) {
		redisTemplate.opsForSet().add("active_sessions", sessionId);
		broadcastMessage("/topic/activeUsers", getActiveUserCount());
	}

	public void broadcastSessionDisconnect(String sessionId) {
		redisTemplate.opsForSet().remove("active_sessions", sessionId);
		broadcastMessage("/topic/activeUsers", getActiveUserCount());
	}

	public void broadcastMessage(String destination, Object message) {
		redisTemplate.convertAndSend("socket:message:" + destination, message);
	}

	public void handleMessage(String destination, Object message) {
		messagingTemplate.convertAndSend(destination, message);
	}

	public int getActiveUserCount() {
		Long count = redisTemplate.opsForSet().size("active_sessions");
		return count != null ? count.intValue() : 0;
	}
}