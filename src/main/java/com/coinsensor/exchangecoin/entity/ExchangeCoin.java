package com.coinsensor.exchangecoin.entity;

import com.coinsensor.coin.entity.Coin;
import com.coinsensor.exchange.entity.Exchange;
import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchange_coins")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExchangeCoin {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_coin_id")
    private Long exchangeCoinId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exchange_id", nullable = false)
    private Exchange exchange;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "exchange_type", nullable = false)
    private ExchangeType exchangeType;
    
    public enum ExchangeType {
        spot, future;
        
        @JsonCreator
        public static ExchangeType fromString(String value) {
            return valueOf(value.toLowerCase());
        }
    }

}
