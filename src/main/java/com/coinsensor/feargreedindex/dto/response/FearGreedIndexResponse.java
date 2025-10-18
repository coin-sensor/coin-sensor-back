package com.coinsensor.feargreedindex.dto.response;

import com.coinsensor.feargreedindex.entity.FearGreedIndex;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FearGreedIndexResponse {
    private Long fearGreedValue;
    private Double volatilityScore;
    private Double dominanceScore;
    private Double sentimentScore;
    private Double newsScore;
    private LocalDateTime createdAt;
    
    public static FearGreedIndexResponse from(FearGreedIndex entity) {
        return FearGreedIndexResponse.builder()
                .fearGreedValue(entity.getFearGreedValue())
                .volatilityScore(entity.getVolatilityScore())
                .dominanceScore(entity.getDominanceScore())
                .sentimentScore(entity.getSentimentScore())
                .newsScore(entity.getNewsScore())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
