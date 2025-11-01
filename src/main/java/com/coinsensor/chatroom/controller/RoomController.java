package com.coinsensor.chatroom.controller;

import com.coinsensor.chatroom.dto.request.RoomRequest;
import com.coinsensor.chatroom.dto.response.RoomResponse;
import com.coinsensor.chatroom.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class RoomController {
    
    private final RoomService roomService;
    
    @PostMapping("/rooms")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }
    
    @GetMapping("/rooms")
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
    
    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        return ResponseEntity.ok(roomService.getRoomById(roomId));
    }
    
    @PutMapping("/rooms/{roomId}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId, @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(roomId, request));
    }
    
    @DeleteMapping("/rooms/{roomId}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return ResponseEntity.ok().build();
    }
}