package com.coinsensor.coin.entity;

import com.coinsensor.exchange.entity.Exchange;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coins")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_id")
    private Long coinId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;
    
    @Column(name = "coin_ticker", nullable = false)
    private String coinTicker;
    
    @Column(name = "base_asset", nullable = false)
    private String baseAsset;
    
    @Column(name = "quote_asset", nullable = false)
    private String quoteAsset;
    
    @Column(nullable = false)
    private String name;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
