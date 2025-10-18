package com.coinsensor.chatmessage.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long roomId;
    private Long userId;
    private String nickname;
    private String message;
}
