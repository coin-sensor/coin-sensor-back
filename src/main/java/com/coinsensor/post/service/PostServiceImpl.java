package com.coinsensor.post.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.category.entity.Category;
import com.coinsensor.category.repository.CategoryRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.post.dto.request.PostCreateRequest;
import com.coinsensor.post.dto.request.PostUpdateRequest;
import com.coinsensor.post.dto.response.PostListResponse;
import com.coinsensor.post.dto.response.PostResponse;
import com.coinsensor.post.entity.Post;
import com.coinsensor.post.repository.PostRepository;
import com.coinsensor.postview.entity.PostView;
import com.coinsensor.postview.repository.PostViewRepository;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	private final PostRepository postRepository;
	private final PostViewRepository postViewRepository;
	private final CategoryRepository categoryRepository;
	private final UserRepository userRepository;

	@Override
	public PostResponse createPost(PostCreateRequest request, String uuid) {
		Category category = categoryRepository.findById(request.getCategoryId())
			.orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Post post = Post.to(category, user, request.getTitle(), request.getContent(), user.getNickname());
		Post savedPost = postRepository.save(post);

		return PostResponse.from(savedPost);
	}

	@Override
	public PostResponse getPost(Long postId, String uuid) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		PostView postView = postViewRepository.findByPostIdAndUuid(postId, uuid);

		if (postView == null) {
			post.incrementViewCount();
			postViewRepository.save(PostView.to(post, user));
		} else {
			postView.increaseViewCount();
		}

		return PostResponse.from(post);
	}

	@Override
	@Transactional(readOnly = true)
	public List<PostListResponse> getPostsByCategoryName(String categoryName) {
		List<Post> posts = postRepository.findByCategoryNameOrderByCreatedAtDesc(categoryName);
		return posts.stream()
			.map(PostListResponse::from)
			.toList();
	}

	@Override
	public PostResponse updatePost(Long postId, PostUpdateRequest request, String uuid) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		if (!post.getUser().getUuid().equals(uuid)) {
			throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
		}

		post.updatePost(request.getTitle(), request.getContent());

		return PostResponse.from(post);
	}

	@Override
	public void deletePost(Long postId, String uuid) {
		Post post = postRepository.findById(postId)
			.orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

		if (!post.getUser().getUuid().equals(uuid)) {
			throw new CustomException(ErrorCode.UNAUTHORIZED_ACCESS);
		}

		postRepository.delete(post);
	}
}