package com.coinsensor.chatmessage.controller;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import com.coinsensor.chatmessage.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageController {
    
    private final ChatMessageService chatMessageService;
    
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getRecentMessages(roomId, limit));
    }
    
    @GetMapping("/rooms/{roomId}/messages/before/{lastMessageId}")
    public ResponseEntity<List<ChatMessageResponse>> getMessagesBefore(
            @PathVariable Long roomId,
            @PathVariable Long lastMessageId,
            @RequestParam(defaultValue = "20") int limit) {
        return ResponseEntity.ok(chatMessageService.getMessagesBefore(roomId, lastMessageId, limit));
    }
    
    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponse> saveMessage(@RequestBody ChatMessageRequest request) {
        return ResponseEntity.ok(chatMessageService.saveMessage(request));
    }
    
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long messageId) {
        chatMessageService.deleteMessage(messageId);
        return ResponseEntity.ok().build();
    }
}
