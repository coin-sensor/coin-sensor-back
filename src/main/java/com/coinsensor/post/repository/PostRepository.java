package com.coinsensor.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.post.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {

	void deleteByPostId(Long postId);

}