package com.coinsensor.post.dto.response;

import java.time.LocalDateTime;

import com.coinsensor.post.entity.Post;

import lombok.Getter;

@Getter
public class PostListResponse {

    private final Long postId;
    private final String title;
    private final String writer;
    private final LocalDateTime createdAt;
    private final Long viewCount;

    private PostListResponse(Long postId, String title, String writer, LocalDateTime createdAt, Long viewCount) {
        this.postId = postId;
        this.title = title;
        this.writer = writer;
        this.createdAt = createdAt;
        this.viewCount = viewCount;
    }

    public static PostListResponse from(Post post) {
        return new PostListResponse(
            post.getPostId(),
            post.getTitle(),
            post.getWriter(),
            post.getCreatedAt(),
            post.getViewCount()
        );
    }
}