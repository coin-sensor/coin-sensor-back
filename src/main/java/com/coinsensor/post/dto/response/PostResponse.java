package com.coinsensor.post.dto.response;

import java.time.LocalDateTime;

import com.coinsensor.post.entity.Post;

import lombok.Getter;

@Getter
public class PostResponse {

    private final Long postId;
    private final Long categoryId;
    private final String categoryName;
    private final Long userId;
    private final String title;
    private final String content;
    private final String writer;
    private final LocalDateTime createdAt;
    private final Long viewCount;

    private PostResponse(Long postId, Long categoryId, String categoryName, Long userId, String title, String content, String writer, LocalDateTime createdAt, Long viewCount) {
        this.postId = postId;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public static PostResponse from(Post post) {
        return new PostResponse(
            post.getPostId(),
            post.getCategory().getCategoryId(),
            post.getCategory().getName(),
            post.getUser().getUserId(),
            post.getTitle(),
            post.getContent(),
            post.getWriter(),
            post.getCreatedAt(),
            post.getViewCount()
        );
    }
}