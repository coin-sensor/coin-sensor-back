package com.coinsensor.message.controller;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService chatMessageService;
    
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<MessageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getRecentMessages(roomId, limit));
    }
    
    @GetMapping("/rooms/{roomId}/messages/before/{lastMessageId}")
    public ResponseEntity<List<MessageResponse>> getMessagesBefore(
            @PathVariable Long roomId,
            @PathVariable Long lastMessageId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getMessagesBefore(roomId, lastMessageId, limit));
    }
    
    @PostMapping("/messages")
    public ResponseEntity<MessageResponse> saveMessage(@RequestBody MessageRequest request) {
        return ResponseEntity.ok(chatMessageService.saveMessage(request));
    }
    
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        chatMessageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
