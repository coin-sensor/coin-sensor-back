package com.coinsensor.newsarticle.controller;

import com.coinsensor.newsarticle.dto.response.NewsArticleResponse;
import com.coinsensor.newsarticle.service.NewsArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
@RequiredArgsConstructor
public class NewsArticleController {
    
    private final NewsArticleService newsArticleService;
    
    @GetMapping
    public ResponseEntity<List<NewsArticleResponse>> getLatestNews() {
        return ResponseEntity.ok(newsArticleService.getLatestNews());
    }
    
    @GetMapping("/{ticker}")
    public ResponseEntity<List<NewsArticleResponse>> getNewsByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(newsArticleService.getNewsByTicker(ticker));
    }
}
