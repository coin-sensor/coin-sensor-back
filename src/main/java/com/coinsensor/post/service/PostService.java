package com.coinsensor.post.service;

import org.springframework.data.domain.Pageable;

import com.coinsensor.common.dto.PageResponse;
import com.coinsensor.post.dto.request.PostCreateRequest;
import com.coinsensor.post.dto.request.PostUpdateRequest;
import com.coinsensor.post.dto.response.PostListResponse;
import com.coinsensor.post.dto.response.PostResponse;

public interface PostService {

	PostResponse createPost(PostCreateRequest request, String uuid);

	PostResponse getPost(Long postId, String uuid);

	PageResponse<PostListResponse> getPostsByCategoryName(String categoryName, Pageable pageable);

	PostResponse updatePost(Long postId, PostUpdateRequest request, String uuid);

	void deletePost(Long postId, String uuid);
}