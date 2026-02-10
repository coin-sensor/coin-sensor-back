package com.coinsensor.post.service;

import java.util.List;

import com.coinsensor.post.dto.request.PostCreateRequest;
import com.coinsensor.post.dto.request.PostUpdateRequest;
import com.coinsensor.post.dto.response.PostListResponse;
import com.coinsensor.post.dto.response.PostResponse;

public interface PostService {

	PostResponse createPost(PostCreateRequest request, String uuid);

	PostResponse getPost(Long postId, String uuid);

	List<PostListResponse> getPostsByCategoryName(String categoryName);

	PostResponse updatePost(Long postId, PostUpdateRequest request, String uuid);

	void deletePost(Long postId, String uuid);
}