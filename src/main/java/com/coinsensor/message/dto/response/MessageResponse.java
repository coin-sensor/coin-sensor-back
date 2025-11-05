package com.coinsensor.message.dto.response;

import com.coinsensor.message.entity.Message;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Long messageId;
    private Long roomId;
    private String uuid;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    
    public static MessageResponse from(Message chatMessage) {
        return MessageResponse.builder()
                .messageId(chatMessage.getMessageId())
                .roomId(chatMessage.getChatRoom().getRoomId())
                .uuid(chatMessage.getUser().getUuid())
                .nickname(chatMessage.getNickname())
                .content(chatMessage.getContent())
                .createdAt(chatMessage.getCreatedAt())
                .build();
    }
}