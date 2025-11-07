package com.coinsensor.message.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class MessageRequest {
    private Long channelId;
    private String uuid;
    private String nickname;
    private String content;

    public MessageRequest(Long channelId, String uuid, String nickname, String content) {
        this.channelId = channelId;
        this.uuid = uuid;
        this.nickname = nickname;
        this.content = content;
    }

    public static MessageRequest to(MessageRequest request, String uuid) {
        return MessageRequest.builder()
                .channelId(request.getChannelId())
                .uuid(uuid)
                .nickname(request.getNickname())
                .content(request.getContent())
                .build();
    }
}