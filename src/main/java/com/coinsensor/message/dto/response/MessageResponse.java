package com.coinsensor.message.dto.response;

import com.coinsensor.message.entity.Message;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class MessageResponse {
    private Long messageId;
    private Long channelId;
    private String uuid;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    
    public static MessageResponse from(Message message) {
        return MessageResponse.builder()
                .messageId(message.getMessageId())
                .channelId(message.getChannel().getChannelId())
                .uuid(message.getUser().getUuid())
                .nickname(message.getNickname())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}