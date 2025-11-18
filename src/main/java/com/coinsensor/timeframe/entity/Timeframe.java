package com.coinsensor.timeframe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "timeframes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Timeframe {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timeframe_id")
    private Long timeframeId;
    
    @Column(nullable = false)
    private String name;
    
    public Timeframe(String name) {
        this.name = name;
    }
}
