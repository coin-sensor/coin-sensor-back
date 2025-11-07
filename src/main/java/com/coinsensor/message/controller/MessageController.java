package com.coinsensor.message.controller;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/channels/{channelId}/messages")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService chatMessageService;
    
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long channelId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getRecentMessages(channelId, limit));
    }

    @GetMapping("/before/{lastMessageId}")
    public ResponseEntity<List<MessageResponse>> getMessagesBefore(
            @PathVariable Long channelId,
            @PathVariable Long lastMessageId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getMessagesBefore(channelId, lastMessageId, limit));
    }
    
    @PostMapping
    public ResponseEntity<MessageResponse> saveMessage(@PathVariable String channelId, @RequestBody MessageRequest request) {
        return ResponseEntity.ok(chatMessageService.saveMessage(request));
    }
    
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String channelId, @PathVariable Long messageId) {
        chatMessageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
