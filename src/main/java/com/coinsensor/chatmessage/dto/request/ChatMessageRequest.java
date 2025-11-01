package com.coinsensor.chatmessage.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@NoArgsConstructor
public class ChatMessageRequest {
    private Long roomId;
    private String uuid;
    private String nickname;
    private String message;

    public ChatMessageRequest(Long roomId, String uuid, String nickname, String message) {
        this.roomId = roomId;
        this.uuid = uuid;
        this.nickname = nickname;
        this.message = message;
    }

    public static ChatMessageRequest to(ChatMessageRequest request, String uuid) {
        return ChatMessageRequest.builder()
                .roomId(request.getRoomId())
                .uuid(uuid)
                .nickname(request.getNickname())
                .message(request.getMessage())
                .build();
    }
}