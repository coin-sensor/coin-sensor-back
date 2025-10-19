package com.coinsensor.chatroom.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;
    
    @Column(name = "room_name", nullable = false)
    private String roomName;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public ChatRoom(String roomName, LocalDateTime createdAt) {
        this.roomName = roomName;
        this.createdAt = createdAt;
    }
}
