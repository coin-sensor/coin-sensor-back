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
@CrossOrigin(origins = "*")
public class ChatMessageController {
    
    private final ChatMessageService chatMessageService;
    
    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatMessageService.getMessagesByRoomId(roomId));
    }
    
    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(@RequestBody ChatMessageRequest request) {
        return ResponseEntity.ok(chatMessageService.sendMessage(request));
    }
}
