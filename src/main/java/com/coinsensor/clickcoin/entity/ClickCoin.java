package com.coinsensor.clickcoin.entity;

import com.coinsensor.user.entity.User;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "click_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ClickCoin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "click_coin_id")
    private Long clickCoinId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_coin_id")
    private ExchangeCoin exchangeCoin;
    
    @Column(nullable = false)
    private Long count;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}