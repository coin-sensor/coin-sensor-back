package com.coinsensor.post.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coinsensor.post.entity.Post;

public interface CustomPostRepository {

	Page<Post> getPostsByCategoryName(String categoryName, Pageable pageable);

	Optional<Post> getByPostId(Long postId);

}