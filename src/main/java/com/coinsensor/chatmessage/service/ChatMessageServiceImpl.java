package com.coinsensor.chatmessage.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import com.coinsensor.chatmessage.entity.ChatMessage;
import com.coinsensor.chatmessage.repository.ChatMessageRepository;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatMessageServiceImpl implements ChatMessageService {
    
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    
    @Override
    @Transactional
    public ChatMessageResponse saveMessage(ChatMessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));
        
        User user = userService.getUserByUuid(request.getUuid());
        return ChatMessageResponse.from(chatMessageRepository.save(ChatMessage.to(chatRoom, user, request)));
    }

    @Override
    public List<ChatMessageResponse> getRecentMessages(Long roomId, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findRecentMessagesByRoomId(roomId, limit);
        return messages.stream()
                .map(ChatMessageResponse::from)
                .sorted(Comparator.comparing(ChatMessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    public List<ChatMessageResponse> getMessagesByRoomId(Long roomId) {
        List<ChatMessage> messages = chatMessageRepository.findRecentMessagesByRoomId(roomId, 50);
        return messages.stream()
                .map(ChatMessageResponse::from)
                .sorted(Comparator.comparing(ChatMessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    public List<ChatMessageResponse> getMessagesBefore(Long roomId, Long lastMessageId, int limit) {
        List<ChatMessage> messages = chatMessageRepository.findMessagesBefore(roomId, lastMessageId, limit);
        return messages.stream()
                .map(ChatMessageResponse::from)
                .sorted(Comparator.comparing(ChatMessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        
        ChatMessage deletedMessage = ChatMessage.builder()
                .messageId(message.getMessageId())
                .chatRoom(message.getChatRoom())
                .user(message.getUser())
                .nickname(message.getNickname())
                .message(message.getMessage())
                .isDeleted(true)
                .createdAt(message.getCreatedAt())
                .build();
        
        chatMessageRepository.save(deletedMessage);
    }
}