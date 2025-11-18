package com.coinsensor.ohlcvs.entity;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.timeframe.entity.Timeframe;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ohlcvs")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ohlcv {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ohlcv_id")
    private Long ohlcvId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exchange_coin_id", nullable = false)
    private ExchangeCoin exchangeCoin;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeframe_id", nullable = false)
    private Timeframe timeframe;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal open;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal high;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal low;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal close;
    
    @Column(nullable = false)
    private Long volume;
    
    @Column(name = "quote_volume", nullable = false, precision = 20, scale = 8)
    private BigDecimal quoteVolume;
    
    @Column(name = "trades_count", nullable = false)
    private Long tradesCount;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Ohlcv(ExchangeCoin exchangeCoin, Timeframe timeframe, BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close, Long volume, BigDecimal quoteVolume, Long tradesCount, LocalDateTime createdAt) {
        this.exchangeCoin = exchangeCoin;
        this.timeframe = timeframe;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.quoteVolume = quoteVolume;
        this.tradesCount = tradesCount;
        this.createdAt = createdAt;
    }
}