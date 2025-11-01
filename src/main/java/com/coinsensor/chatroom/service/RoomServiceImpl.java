package com.coinsensor.chatroom.service;

import com.coinsensor.chatroom.dto.request.RoomRequest;
import com.coinsensor.chatroom.dto.response.RoomResponse;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomServiceImpl implements RoomService {
    
    private final ChatRoomRepository chatRoomRepository;
    
    @Override
    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getRoomName())
                .createdAt(LocalDateTime.now())
                .build();
        
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return RoomResponse.from(savedRoom);
    }
    
    @Override
    public List<RoomResponse> getAllRooms() {
        return chatRoomRepository.findAll().stream()
                .map(RoomResponse::from)
                .toList();
    }
    
    @Override
    public RoomResponse getRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        return RoomResponse.from(chatRoom);
    }
    
    @Override
    @Transactional
    public RoomResponse updateRoom(Long roomId, RoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("채팅방을 찾을 수 없습니다."));
        
        ChatRoom updatedRoom = ChatRoom.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(request.getRoomName())
                .createdAt(chatRoom.getCreatedAt())
                .build();
        
        ChatRoom savedRoom = chatRoomRepository.save(updatedRoom);
        return RoomResponse.from(savedRoom);
    }
    
    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new RuntimeException("채팅방을 찾을 수 없습니다.");
        }
        chatRoomRepository.deleteById(roomId);
    }
}