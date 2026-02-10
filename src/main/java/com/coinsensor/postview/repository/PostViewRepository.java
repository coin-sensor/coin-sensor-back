package com.coinsensor.postview.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.postview.entity.PostView;

public interface PostViewRepository extends JpaRepository<PostView, Long>, CustomPostViewRepository {

}