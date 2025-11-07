package com.coinsensor.channel.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.coinsensor.channel.dto.request.ChannelRequest;

@Entity
@Table(name = "channels")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Channel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private Long channelId;
    
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Channel(String name) {
        this.name = name;
        this.isDeleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public static Channel to(ChannelRequest request) {
        return new Channel(request.getName());
    }

    public void updateName(String name) {
        this.name = name;
    }
}
