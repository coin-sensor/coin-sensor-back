package com.coinsensor.chatroom.controller;

import com.coinsensor.chatroom.dto.request.ChatRoomRequest;
import com.coinsensor.chatroom.dto.response.ChatRoomResponse;
import com.coinsensor.chatroom.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {
    
    private final ChatRoomService roomService;
    
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomResponse> createRoom(@RequestBody ChatRoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }
    
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomResponse> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
    
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<ChatRoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody ChatRoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, request));
    }
    
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}