package com.coinsensor.post.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.common.annotation.AuthorizeRole;
import com.coinsensor.common.dto.ApiResponse;
import com.coinsensor.common.dto.PageResponse;
import com.coinsensor.post.dto.request.PostCreateRequest;
import com.coinsensor.post.dto.request.PostUpdateRequest;
import com.coinsensor.post.dto.response.PostListResponse;
import com.coinsensor.post.dto.response.PostResponse;
import com.coinsensor.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@AuthorizeRole
	@PostMapping
	public ResponseEntity<ApiResponse<PostResponse>> createPost(
		@RequestBody PostCreateRequest request, @RequestHeader String uuid) {
		PostResponse response = postService.createPost(request, uuid);
		return ApiResponse.createSuccess(response);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<PageResponse<PostListResponse>>> getPosts(
		@RequestParam String categoryName, @PageableDefault(size = 10) Pageable pageable) {
		PageResponse<PostListResponse> response = postService.getPostsByCategoryName(categoryName, pageable);
		return ApiResponse.createSuccess(response);
	}

	@GetMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostResponse>> getPost(@PathVariable Long postId, @RequestHeader String uuid) {
		PostResponse response = postService.getPost(postId, uuid);
		return ApiResponse.createSuccess(response);
	}

	@AuthorizeRole
	@PutMapping("/{postId}")
	public ResponseEntity<ApiResponse<PostResponse>> updatePost(
		@PathVariable Long postId, @RequestBody PostUpdateRequest request, @RequestHeader String uuid) {
		PostResponse response = postService.updatePost(postId, request, uuid);
		return ApiResponse.createSuccess(response);
	}

	@AuthorizeRole
	@DeleteMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> deletePost(
		@PathVariable Long postId, @RequestHeader String uuid) {
		postService.deletePost(postId, uuid);
		return ApiResponse.createSuccess(null);
	}
}