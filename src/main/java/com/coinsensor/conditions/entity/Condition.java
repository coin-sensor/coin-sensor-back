package com.coinsensor.conditions.entity;

import com.coinsensor.timeframe.entity.Timeframe;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "conditions")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Condition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "condition_id")
    private Long conditionId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeframe_id", nullable = false)
    private Timeframe timeframe;
    
    @Column(name = "change_x", nullable = false, precision = 10, scale = 2)
    private BigDecimal changeX;
    
    @Column(name = "volume_x", nullable = false, precision = 10, scale = 2)
    private BigDecimal volumeX;
    
    public Condition(Timeframe timeframe, BigDecimal changeX, BigDecimal volumeX) {
        this.timeframe = timeframe;
        this.changeX = changeX;
        this.volumeX = volumeX;
    }
}