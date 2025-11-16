// package com.coinsensor.websocket;
//
// import com.fasterxml.jackson.databind.ObjectMapper;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Component;
// import org.springframework.web.socket.*;
// import java.util.concurrent.ConcurrentHashMap;
// import java.util.concurrent.CopyOnWriteArraySet;
//
// @Component
// @RequiredArgsConstructor
// @Slf4j
// public class WebSocketHandler implements org.springframework.web.socket.WebSocketHandler {
//
//     private final ObjectMapper objectMapper;
//     private final ConcurrentHashMap<String, CopyOnWriteArraySet<WebSocketSession>> subscriptions = new ConcurrentHashMap<>();
//
//     @Override
//     public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//         log.info("WebSocket connection established: {}", session.getId());
//     }
//
//     @Override
//     public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//         String payload = message.getPayload().toString();
//         log.debug("Received message: {}", payload);
//
//         // 구독 처리
//         if (payload.startsWith("SUBSCRIBE:")) {
//             String topic = payload.substring(10);
//             subscribe(session, topic);
//         } else if (payload.startsWith("UNSUBSCRIBE:")) {
//             String topic = payload.substring(12);
//             unsubscribe(session, topic);
//         } else if (payload.startsWith("CHAT:")) {
//             handleChatMessage(session, payload);
//         }
//     }
//
//     @Override
//     public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
//         log.error("WebSocket transport error for session {}: {}", session.getId(), exception.getMessage());
//     }
//
//     @Override
//     public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
//         log.info("WebSocket connection closed: {} with status: {}", session.getId(), closeStatus);
//         removeSessionFromAllSubscriptions(session);
//     }
//
//     @Override
//     public boolean supportsPartialMessages() {
//         return false;
//     }
//
//     private void subscribe(WebSocketSession session, String topic) {
//         subscriptions.computeIfAbsent(topic, k -> new CopyOnWriteArraySet<>()).add(session);
//         log.info("Session {} subscribed to topic: {}", session.getId(), topic);
//     }
//
//     private void unsubscribe(WebSocketSession session, String topic) {
//         CopyOnWriteArraySet<WebSocketSession> sessions = subscriptions.get(topic);
//         if (sessions != null) {
//             sessions.remove(session);
//             if (sessions.isEmpty()) {
//                 subscriptions.remove(topic);
//             }
//         }
//         log.info("Session {} unsubscribed from topic: {}", session.getId(), topic);
//     }
//
//     private void removeSessionFromAllSubscriptions(WebSocketSession session) {
//         subscriptions.values().forEach(sessions -> sessions.remove(session));
//     }
//
//     public void broadcastCoinData(Object coinData) {
//         try {
//             String message = objectMapper.writeValueAsString(coinData);
//             broadcast("coins", message);
//         } catch (Exception e) {
//             log.error("Failed to broadcast coin data", e);
//         }
//     }
//
//     private void handleChatMessage(WebSocketSession session, String payload) {
//         try {
//             String uuid = (String) session.getAttributes().get("uuid");
//             String chatMessage = payload.substring(5); // "CHAT:" 제거
//
//             // TODO: DB에 채팅 메시지 저장
//             // chatService.saveChatMessage(uuid, chatMessage);
//
//             // 채팅방의 모든 사용자에게 브로드캐스트
//             String broadcastMessage = String.format("{\"type\":\"chat\",\"uuid\":\"%s\",\"message\":\"%s\"}", uuid, chatMessage);
//             broadcast("chat", broadcastMessage);
//
//             log.info("Chat message from {}: {}", uuid, chatMessage);
//         } catch (Exception e) {
//             log.error("Failed to handle chat message", e);
//         }
//     }
//
//     private void broadcast(String topic, String message) {
//         CopyOnWriteArraySet<WebSocketSession> sessions = subscriptions.get(topic);
//         if (sessions != null) {
//             sessions.removeIf(session -> {
//                 try {
//                     if (session.isOpen()) {
//                         session.sendMessage(new TextMessage(message));
//                         return false;
//                     } else {
//                         return true;
//                     }
//                 } catch (Exception e) {
//                     log.warn("Failed to send message to session {}: {}", session.getId(), e.getMessage());
//                     return true;
//                 }
//             });
//         }
//     }
// }