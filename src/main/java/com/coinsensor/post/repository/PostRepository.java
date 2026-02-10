package com.coinsensor.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByCategoryNameOrderByCreatedAtDesc(String categoryName);

	void deleteByPostId(Long postId);

}