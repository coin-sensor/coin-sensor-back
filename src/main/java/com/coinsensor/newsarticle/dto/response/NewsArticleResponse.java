package com.coinsensor.newsarticle.dto.response;

import com.coinsensor.newsarticle.entity.NewsArticle;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticleResponse {
    private Long newsArticleId;
    private String title;
    private String summary;
    private String sourceName;
    private String sourceUrl;
    private LocalDateTime publishedAt;
    private String relatedTicker;
    private String sentiment;
    
    public static NewsArticleResponse from(NewsArticle entity) {
        return NewsArticleResponse.builder()
                .newsArticleId(entity.getNewsArticleId())
                .title(entity.getTitle())
                .summary(entity.getSummary())
                .sourceName(entity.getSourceName())
                .sourceUrl(entity.getSourceUrl())
                .publishedAt(entity.getPublishedAt())
                .relatedTicker(entity.getRelatedTicker())
                .sentiment(entity.getSentiment() != null ? entity.getSentiment().name() : null)
                .build();
    }
}
