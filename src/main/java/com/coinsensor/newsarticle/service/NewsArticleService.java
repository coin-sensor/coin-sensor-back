package com.coinsensor.newsarticle.service;

import com.coinsensor.newsarticle.dto.response.NewsArticleResponse;
import java.util.List;

public interface NewsArticleService {
    List<NewsArticleResponse> getLatestNews();
    List<NewsArticleResponse> getNewsByTicker(String ticker);
}
