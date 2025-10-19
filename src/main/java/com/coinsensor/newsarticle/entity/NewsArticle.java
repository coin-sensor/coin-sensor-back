package com.coinsensor.newsarticle.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news_articles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class NewsArticle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_article_id")
    private Long newsArticleId;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String summary;
    
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    
    @Column(name = "source_name")
    private String sourceName;
    
    @Column(name = "source_url")
    private String sourceUrl;
    
    @Column(name = "published_at", nullable = false)
    private LocalDateTime publishedAt;
    
    @Column(name = "related_ticker")
    private String relatedTicker;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('positive', 'neutral', 'negative')")
    private Sentiment sentiment;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    public NewsArticle(String title, String summary, String content, String sourceName, String sourceUrl, LocalDateTime publishedAt, String relatedTicker, Sentiment sentiment, LocalDateTime createdAt) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.sourceName = sourceName;
        this.sourceUrl = sourceUrl;
        this.publishedAt = publishedAt;
        this.relatedTicker = relatedTicker;
        this.sentiment = sentiment;
        this.createdAt = createdAt;
    }
    
    public enum Sentiment {
        positive, neutral, negative
    }
}
