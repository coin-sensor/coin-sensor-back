package com.coinsensor.chatmessage.dto.response;

import com.coinsensor.chatmessage.entity.ChatMessage;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long roomId;
    private Long userId;
    private String nickname;
    private String message;
    private LocalDateTime createdAt;
    
    public static ChatMessageResponse from(ChatMessage chatMessage) {
        return ChatMessageResponse.builder()
                .messageId(chatMessage.getMessageId())
                .roomId(chatMessage.getChatRoom().getRoomId())
                .userId(chatMessage.getUser().getUserId())
                .nickname(chatMessage.getNickname())
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}
