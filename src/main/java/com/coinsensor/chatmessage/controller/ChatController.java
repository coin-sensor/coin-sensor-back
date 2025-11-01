package com.coinsensor.chatmessage.controller;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import com.coinsensor.chatmessage.service.ChatMessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/chat/send")
    public void sendMessage(@Payload ChatMessageRequest request, @Header String uuid) {
        ChatMessageResponse response = chatMessageService.saveMessage(ChatMessageRequest.to(request, uuid));
        messagingTemplate.convertAndSend("/topic/chat/rooms/" + request.getRoomId(), response);
    }
}