package com.coinsensor.chatmessage.service;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import com.coinsensor.chatmessage.entity.ChatMessage;
import com.coinsensor.chatmessage.repository.ChatMessageRepository;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    
    @Override
    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        return chatMessageRepository.findAll().stream()
                .filter(msg -> msg.getChatRoom().getRoomId().equals(roomId) && !msg.getIsDeleted())
                .map(ChatMessageResponse::from)
                .toList();
    }
    
    @Override
    @Transactional
    public ChatMessageResponse sendMessage(ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .user(user)
                .nickname(request.getNickname())
                .message(request.getMessage())
                .isDeleted(false)
                .createdAt(LocalDateTime.now())
                .build();
        
        return ChatMessageResponse.from(chatMessageRepository.save(message));
    }
}
