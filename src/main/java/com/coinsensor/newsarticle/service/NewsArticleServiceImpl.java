package com.coinsensor.newsarticle.service;

import com.coinsensor.newsarticle.dto.response.NewsArticleResponse;
import com.coinsensor.newsarticle.repository.NewsArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsArticleServiceImpl implements NewsArticleService {
    
    private final NewsArticleRepository newsArticleRepository;
    
    @Override
    public List<NewsArticleResponse> getLatestNews() {
        return newsArticleRepository.findAll().stream()
                .map(NewsArticleResponse::from)
                .toList();
    }
    
    @Override
    public List<NewsArticleResponse> getNewsByTicker(String ticker) {
        return newsArticleRepository.findAll().stream()
                .filter(news -> ticker.equals(news.getRelatedTicker()))
                .map(NewsArticleResponse::from)
                .toList();
    }
}
