package com.coinsensor.userignore.entity;

import com.coinsensor.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_ignores")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserIgnore {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_ignore_id")
    private Long userIgnoreId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_target_id")
    private User targetUser;
}