package com.coinsensor.message.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import com.coinsensor.message.entity.Message;
import com.coinsensor.message.repository.MessageRepository;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.chatroom.repository.ChatRoomRepository;
import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {
    
    private final MessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    
    @Override
    @Transactional
    public MessageResponse saveMessage(MessageRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new BusinessException(ROOM_NOT_FOUND));
        
        User user = userService.getUserByUuid(request.getUuid());
        return MessageResponse.from(chatMessageRepository.save(Message.to(chatRoom, user, request)));
    }

    @Override
    public List<MessageResponse> getRecentMessages(Long roomId, int limit) {
        List<Message> messages = chatMessageRepository.findRecentMessagesByRoomId(roomId, limit);
        return messages.stream()
                .map(MessageResponse::from)
                .sorted(Comparator.comparing(MessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    public List<MessageResponse> getMessagesByRoomId(Long roomId) {
        List<Message> messages = chatMessageRepository.findRecentMessagesByRoomId(roomId, 50);
        return messages.stream()
                .map(MessageResponse::from)
                .sorted(Comparator.comparing(MessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    public List<MessageResponse> getMessagesBefore(Long roomId, Long lastMessageId, int limit) {
        List<Message> messages = chatMessageRepository.findMessagesBefore(roomId, lastMessageId, limit);
        return messages.stream()
                .map(MessageResponse::from)
                .sorted(Comparator.comparing(MessageResponse::getCreatedAt))
                .toList();
    }
    
    @Override
    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("메시지를 찾을 수 없습니다."));
        
        Message deletedMessage = Message.builder()
                .messageId(message.getMessageId())
                .chatRoom(message.getChatRoom())
                .user(message.getUser())
                .nickname(message.getNickname())
                .content(message.getContent())
                .isDeleted(true)
                .createdAt(message.getCreatedAt())
                .build();
        
        chatMessageRepository.save(deletedMessage);
    }
}