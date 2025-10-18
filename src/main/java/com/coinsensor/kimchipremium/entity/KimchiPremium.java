package com.coinsensor.kimchipremium.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "kimchi_premium")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KimchiPremium {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kimchi_premium_id")
    private Long kimchiPremiumId;
    
    @Column(name = "binance_btc_usdt", nullable = false)
    private Double binanceBtcUsdt;
    
    @Column(name = "upbit_btc_krw", nullable = false)
    private Double upbitBtcKrw;
    
    @Column(name = "kimchi_premium", nullable = false)
    private Double kimchiPremium;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
