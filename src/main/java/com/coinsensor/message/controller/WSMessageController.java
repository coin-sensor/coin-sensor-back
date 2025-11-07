package com.coinsensor.message.controller;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.service.MessageService;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WSMessageController {

    private final MessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ws/channel/send")
    public void sendMessage(@Payload MessageRequest request, @Header String uuid) {
        MessageResponse response = chatMessageService.saveMessage(MessageRequest.to(request, uuid));
        messagingTemplate.convertAndSend("/topic/channels/" + request.getChannelId(), response);
    }
}