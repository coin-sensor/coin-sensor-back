package com.coinsensor.analysis.dto.response;

import com.coinsensor.analysis.entity.Analysis;
import lombok.*;
import java.time.LocalDate;

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
    
    public static AnalysisResponse from(Analysis entity) {
        return AnalysisResponse.builder()
                .reportId(entity.getReportId())
                .reportDate(entity.getReportDate())
                .summaryText(entity.getSummaryText())
                .trendPrediction(entity.getTrendPrediction() != null ? entity.getTrendPrediction().name() : null)
                .isVolatilityAlert(entity.getIsVolatilityAlert())
                .recommendation(entity.getRecommendation())
                .build();
    }
}
