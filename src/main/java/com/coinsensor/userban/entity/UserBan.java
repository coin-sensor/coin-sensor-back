package com.coinsensor.userban.entity;

import com.coinsensor.bantype.entity.BanType;
import com.coinsensor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_bans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserBan {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ban_id")
    private Long userBanId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ban_type_id")
    private BanType banType;
    
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    public static UserBan to(User user, BanType banType, LocalDateTime startTime, LocalDateTime endTime) {
        return UserBan.builder()
                .user(user)
                .banType(banType)
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }
}