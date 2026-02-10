package com.coinsensor.postview.repository;

import com.coinsensor.postview.entity.PostView;

public interface CustomPostViewRepository {

	PostView findByPostIdAndUuid(Long postId, String uuid);
}