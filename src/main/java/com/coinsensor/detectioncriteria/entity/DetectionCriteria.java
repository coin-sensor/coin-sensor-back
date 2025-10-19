package com.coinsensor.detectioncriteria.entity;

import com.coinsensor.timeframe.entity.Timeframe;
import jakarta.persistence.*;
import lombok.*;

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
    
    @Column(nullable = false)
    private Double volatility;
    
    @Column(nullable = false)
    private Double volume;
    
    public DetectionCriteria(Timeframe timeframe, Double volatility, Double volume) {
        this.timeframe = timeframe;
        this.volatility = volatility;
        this.volume = volume;
    }
}
