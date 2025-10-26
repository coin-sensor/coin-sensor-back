package com.coinsensor.exchange.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exchanges")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Exchange {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exchange_id")
    private Long exchangeId;
    
    @Column(nullable = false)
    private String name;
    
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
    
    public Exchange(String name, ExchangeType exchangeType) {
        this.name = name;
        this.exchangeType = exchangeType;
    }
}
