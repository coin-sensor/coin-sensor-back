package com.coinsensor.chatroom.service;

import com.coinsensor.chatroom.dto.request.ChatRoomRequest;
import com.coinsensor.chatroom.dto.response.ChatRoomResponse;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {
    
    private final ChatRoomRepository chatRoomRepository;
    
    @Override
    @Transactional
    public ChatRoomResponse createRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = ChatRoom.builder()
                .roomName(request.getRoomName())
                .build();
        
        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponse.from(savedRoom);
    }
    
    @Override
    public List<ChatRoomResponse> getAllRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomResponse::from)
                .toList();
    }
    
    @Override
    public ChatRoomResponse getRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));
        return ChatRoomResponse.from(chatRoom);
    }
    
    @Override
    @Transactional
    public ChatRoomResponse updateRoom(Long roomId, ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new BusinessException(ErrorCode.ROOM_NOT_FOUND));

        chatRoom.updateRoomName(request.getRoomName());
        return ChatRoomResponse.from(chatRoom);
    }
    
    @Override
    @Transactional
    public void deleteRoom(Long roomId) {
        if (!chatRoomRepository.existsById(roomId)) {
            throw new BusinessException(ErrorCode.ROOM_NOT_FOUND);
        }
        chatRoomRepository.deleteById(roomId);
    }
}