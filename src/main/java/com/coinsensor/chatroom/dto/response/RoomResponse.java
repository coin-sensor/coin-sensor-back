package com.coinsensor.chatroom.dto.response;

import com.coinsensor.chatroom.entity.ChatRoom;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@Builder
public class RoomResponse {
    private Long roomId;
    private String roomName;
    private LocalDateTime createdAt;
    
    public static RoomResponse from(ChatRoom chatRoom) {
        return RoomResponse.builder()
                .roomId(chatRoom.getRoomId())
                .roomName(chatRoom.getRoomName())
                .createdAt(chatRoom.getCreatedAt())
                .build();
    }
}