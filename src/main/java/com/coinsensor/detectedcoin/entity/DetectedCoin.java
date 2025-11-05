package com.coinsensor.detectedcoin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.detectiongroup.entity.DetectionGroup;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

@Entity
@Table(name = "detected_coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetectedCoin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detected_coin_id")
    private Long detectedCoinId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "detection_group_id", nullable = false)
    private DetectionGroup detectionGroup;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exchange_coin_id", nullable = false)
    private ExchangeCoin exchangeCoin;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal volatility;
    
    @Column(nullable = false)
    private Double volume;
    
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt;
    
    public DetectedCoin(DetectionGroup detectionGroup, Coin coin, ExchangeCoin exchangeCoin, BigDecimal volatility, Double volume, LocalDateTime detectedAt) {
        this.detectionGroup = detectionGroup;
        this.coin = coin;
        this.exchangeCoin = exchangeCoin;
        this.volatility = volatility;
        this.volume = volume;
        this.detectedAt = detectedAt;
    }
}
