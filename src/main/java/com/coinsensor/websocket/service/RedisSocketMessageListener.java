package com.coinsensor.websocket.service;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSocketMessageListener implements MessageListener {

    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final SocketSyncService socketSyncService;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        redisMessageListenerContainer.addMessageListener(this, new PatternTopic("socket:message:*"));
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String channel = new String(message.getChannel());
            String body = new String(message.getBody());
            String destination = channel.substring("socket:message:".length());
            Object messageObj = objectMapper.readValue(body, Object.class);
            socketSyncService.handleMessage(destination, messageObj);
        } catch (Exception e) {
            log.error("Error processing Redis socket message", e);
        }
    }
}