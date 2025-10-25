package com.coinsensor.detectioncriteria.entity;

import com.coinsensor.timeframe.entity.Timeframe;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detection_criteria")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class DetectionCriteria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_criteria_id")
    private Long detectionCriteriaId;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "timeframe_id", nullable = false)
    private Timeframe timeframe;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal volatility;
    
    @Column(nullable = false)
    private Double volume;
    
    public DetectionCriteria(Timeframe timeframe, BigDecimal volatility, Double volume) {
        this.timeframe = timeframe;
        this.volatility = volatility;
        this.volume = volume;
    }
}
