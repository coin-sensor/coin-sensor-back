package com.coinsensor.btcaireport.dto.response;

import com.coinsensor.btcaireport.entity.BtcAiReport;
import lombok.*;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BtcAiReportResponse {
    private Long reportId;
    private LocalDate reportDate;
    private String summaryText;
    private String trendPrediction;
    private Boolean isVolatilityAlert;
    private String recommendation;
    
    public static BtcAiReportResponse from(BtcAiReport entity) {
        return BtcAiReportResponse.builder()
                .reportId(entity.getReportId())
                .reportDate(entity.getReportDate())
                .summaryText(entity.getSummaryText())
                .trendPrediction(entity.getTrendPrediction() != null ? entity.getTrendPrediction().name() : null)
                .isVolatilityAlert(entity.getIsVolatilityAlert())
                .recommendation(entity.getRecommendation())
                .build();
    }
}
