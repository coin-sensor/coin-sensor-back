package com.coinsensor.detectedcoin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.detectiongroup.entity.DetectionGroup;

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
    
    @Column(nullable = false)
    private Double volatility;
    
    @Column(nullable = false)
    private Double volume;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
