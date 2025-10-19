package com.coinsensor.coin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coins")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private Long coinId;
    
    @Column(name = "coin_ticker", nullable = false)
    private String coinTicker;
    
    @Column(name = "base_asset", nullable = false)
    private String baseAsset;
}
