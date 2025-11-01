package com.coinsensor.chatmessage.entity;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private ChatRoom chatRoom;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String nickname;
    
    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public ChatMessage(ChatRoom chatRoom, User user, String nickname, String message, Boolean isDeleted, LocalDateTime createdAt) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.nickname = nickname;
        this.message = message;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static ChatMessage to(ChatRoom chatRoom, User user, ChatMessageRequest request) {
        return ChatMessage.builder()
            .chatRoom(chatRoom)
            .user(user)
            .nickname(request.getNickname())
            .message(request.getMessage())
            .isDeleted(false)
            .createdAt(LocalDateTime.now())
            .build();
    }


}
