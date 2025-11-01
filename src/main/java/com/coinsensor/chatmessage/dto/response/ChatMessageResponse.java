package com.coinsensor.chatmessage.dto.response;

import com.coinsensor.chatmessage.entity.ChatMessage;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private String uuid;
    private String nickname;
    private String message;
    private LocalDateTime createdAt;
    
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .messageId(chatMessage.getMessageId())
                .roomId(chatMessage.getChatRoom().getRoomId())
                .uuid(chatMessage.getUser().getUuid())
                .nickname(chatMessage.getNickname())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}