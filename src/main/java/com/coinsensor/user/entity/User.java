package com.coinsensor.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    @Column(nullable = false)
    private String uuid;
    
    @Column(name = "ip_address", nullable = false)
    private String ipAddress;
    
    @Column(name = "is_banned", nullable = false)
    private Boolean isBanned;
    
    @Column(name = "last_active", nullable = false)
    private LocalDateTime lastActive;
    
    @Column(nullable = false)
    private String nickname;
    
    @Column(nullable = false)
    private String role;
    
    @Column(nullable = false)
    private Boolean notification;
    
    public User(String uuid, String ipAddress, Boolean isBanned, LocalDateTime lastActive, String nickname, String role) {
        this.uuid = uuid;
        this.ipAddress = ipAddress;
        this.isBanned = isBanned;
        this.lastActive = lastActive;
        this.nickname = nickname;
        this.role = role;
        this.notification = true;
    }

    public static User to(String uuid, String ipAddress) {
        return User.builder()
            .uuid(uuid)
            .ipAddress(ipAddress)
            .isBanned(false)
            .lastActive(LocalDateTime.now())
            .nickname("트레이더#" + uuid.substring(0, 4))
            .role("member")
            .notification(true)
            .build();
    }
}