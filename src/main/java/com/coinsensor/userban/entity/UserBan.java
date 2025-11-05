package com.coinsensor.userban.entity;

import com.coinsensor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_target_id")
    private User targetUser;
}