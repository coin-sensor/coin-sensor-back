package com.coinsensor.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "btc_ai_analysiss")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Analysis {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analysis_id")
    private Long analysisId;
    
    @Column(name = "analysis_date", nullable = false)
    private LocalDate analysisDate;
    
    @Column(name = "summary_text", columnDefinition = "TEXT")
    private String summaryText;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "trend_prediction", columnDefinition = "ENUM('bullish', 'bearish', 'neutral')")
    private TrendPrediction trendPrediction;
    
    @Column(name = "is_volatility_alert")
    private Boolean isVolatilityAlert;
    
    @Column(columnDefinition = "TEXT")
    private String recommendation;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public Analysis(LocalDate analysisDate, String summaryText, TrendPrediction trendPrediction, Boolean isVolatilityAlert, String recommendation, LocalDateTime createdAt) {
        this.analysisDate = analysisDate;
        this.summaryText = summaryText;
        this.trendPrediction = trendPrediction;
        this.isVolatilityAlert = isVolatilityAlert;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
    }
    
    public enum TrendPrediction {
        bullish, bearish, neutral
    }
}
