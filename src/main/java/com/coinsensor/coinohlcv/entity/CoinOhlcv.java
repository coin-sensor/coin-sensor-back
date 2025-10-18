package com.coinsensor.coinohlcv.entity;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.timeframe.entity.Timeframe;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coin_ohlcvs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoinOhlcv {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coin_ohlcv_id")
    private Long coinOhlcvId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeframe_id", nullable = false)
    private Timeframe timeframe;
    
    @Column(nullable = false)
    private Double open;
    
    @Column(nullable = false)
    private Double high;
    
    @Column(nullable = false)
    private Double low;
    
    @Column(nullable = false)
    private Double close;
    
    @Column(nullable = false)
    private Double volume;
    
    @Column(name = "quote_volume")
    private Double quoteVolume;
    
    @Column(name = "trades_count")
    private Integer tradesCount;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
