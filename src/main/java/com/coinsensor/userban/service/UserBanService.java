package com.coinsensor.userban.service;

import java.util.List;

import com.coinsensor.userban.dto.request.UserBanRequest;
import com.coinsensor.userban.dto.response.UserBanResponse;

public interface UserBanService {

	UserBanResponse banUser(UserBanRequest request);

	List<UserBanResponse> getAllBannedUsers();

	List<UserBanResponse> getUserBans(Long userId);

	UserBanResponse getActiveBan(Long userId);

	UserBanResponse getActiveBanByUuid(String uuid);

	boolean isBannedByUuid(String uuid);

	void unbanUser(Long userBanId);
}