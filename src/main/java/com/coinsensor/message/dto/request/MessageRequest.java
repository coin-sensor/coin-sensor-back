package com.coinsensor.message.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MessageRequest {
    private Long roomId;
    private String uuid;
    private String nickname;
    private String content;

    public MessageRequest(Long roomId, String uuid, String nickname, String content) {
        this.roomId = roomId;
        this.uuid = uuid;
        this.nickname = nickname;
        this.content = content;
    }

    public static MessageRequest to(MessageRequest request, String uuid) {
        return MessageRequest.builder()
                .roomId(request.getRoomId())
                .uuid(uuid)
                .nickname(request.getNickname())
                .content(request.getContent())
                .build();
    }
}