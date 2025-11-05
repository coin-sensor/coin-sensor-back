package com.coinsensor.favoritecoin.entity;

import com.coinsensor.user.entity.User;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteCoin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_coin_id")
    private Long favoriteCoinId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_coin_id")
    private ExchangeCoin exchangeCoin;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}