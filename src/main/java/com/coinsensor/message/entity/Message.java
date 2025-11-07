package com.coinsensor.message.entity;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.channel.entity.Channel;
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
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    
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
    
    public Message(Channel channel, User user, String nickname, String content, Boolean isDeleted, LocalDateTime createdAt) {
        this.channel = channel;
        this.user = user;
        this.nickname = nickname;
        this.content = content;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
    }

    public static Message to(Channel channel, User user, MessageRequest request) {
        return Message.builder()
            .channel(channel)
            .user(user)
            .nickname(request.getNickname())
            .content(request.getContent())
            .isDeleted(false)
            .createdAt(LocalDateTime.now())
            .build();
    }


}
