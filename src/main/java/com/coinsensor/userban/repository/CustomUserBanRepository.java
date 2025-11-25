package com.coinsensor.userban.repository;

import com.coinsensor.userban.dto.response.UserBanResponse;

public interface CustomUserBanRepository {

	boolean existsActiveBanByUuid(String uuid);

	UserBanResponse findActiveBanByUserId(Long userId);

	UserBanResponse findActiveBanByUuid(String uuid);
}