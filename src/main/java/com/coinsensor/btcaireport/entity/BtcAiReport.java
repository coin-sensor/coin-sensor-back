package com.coinsensor.btcaireport.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "btc_ai_reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BtcAiReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;
    
    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;
    
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
    
    public BtcAiReport(LocalDate reportDate, String summaryText, TrendPrediction trendPrediction, Boolean isVolatilityAlert, String recommendation, LocalDateTime createdAt) {
        this.reportDate = reportDate;
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
