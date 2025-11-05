package com.coinsensor.analysis.dto.response;

import lombok.*;
import java.time.LocalDate;

import com.coinsensor.analysis.entity.Analysis;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisResponse {
    private Long reportId;
    private LocalDate reportDate;
    private String summaryText;
    private String trendPrediction;
    private Boolean isVolatilityAlert;
    private String recommendation;
    
    public static AnalysisResponse from(Analysis analysis) {
        return AnalysisResponse.builder()
                .reportId(analysis.getAnalysisId())
                .summaryText(analysis.getSummaryText())
                .trendPrediction(analysis.getTrendPrediction() != null ? analysis.getTrendPrediction().name() : null)
                .isVolatilityAlert(analysis.getIsVolatilityAlert())
                .recommendation(analysis.getRecommendation())
                .build();
    }
}
