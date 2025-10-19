package com.coinsensor.exchange.entity;

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
    
    public Exchange(String name) {
        this.name = name;
    }
}
