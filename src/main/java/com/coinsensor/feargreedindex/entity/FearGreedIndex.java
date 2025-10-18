package com.coinsensor.feargreedindex.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fear_greed_indexes")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FearGreedIndex {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fear_greed_index_id")
    private Long fearGreedIndexId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "fear_greed_value", nullable = false)
    private Long fearGreedValue;
    
    @Column(name = "volatility_score")
    private Double volatilityScore;
    
    @Column(name = "dominance_score")
    private Double dominanceScore;
    
    @Column(name = "sentiment_score")
    private Double sentimentScore;
    
    @Column(name = "news_score")
    private Double newsScore;
}
