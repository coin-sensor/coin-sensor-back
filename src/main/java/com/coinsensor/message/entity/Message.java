package com.coinsensor.message.entity;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.chatroom.entity.ChatRoom;
import com.coinsensor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Message {
    
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
    private String content;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Message(ChatRoom chatRoom, User user, String nickname, String content, Boolean isDeleted, LocalDateTime createdAt) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.nickname = nickname;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static Message to(ChatRoom chatRoom, User user, MessageRequest request) {
        return Message.builder()
            .chatRoom(chatRoom)
            .user(user)
            .nickname(request.getNickname())
            .content(request.getContent())
            .isDeleted(false)
            .createdAt(LocalDateTime.now())
            .build();
    }


}
