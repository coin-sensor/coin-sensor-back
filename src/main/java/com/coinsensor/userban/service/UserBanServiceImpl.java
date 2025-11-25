package com.coinsensor.userban.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.bantype.entity.BanType;
import com.coinsensor.bantype.repository.BanTypeRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.userban.dto.request.UserBanRequest;
import com.coinsensor.userban.dto.response.UserBanResponse;
import com.coinsensor.userban.entity.UserBan;
import com.coinsensor.userban.repository.UserBanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserBanServiceImpl implements UserBanService {

	private final UserBanRepository userBanRepository;
	private final UserRepository userRepository;
	private final BanTypeRepository banTypeRepository;

	@Override
	public UserBanResponse banUser(UserBanRequest request) {
		User user = userRepository.findByUserId(request.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		// 이미 활성화된 금지가 있는지 확인
		if (userBanRepository.findActiveBanByUserId(request.getUserId()) != null) {
			throw new CustomException(ErrorCode.ALREADY_EXISTS);
		}

		BanType banType = banTypeRepository.findById(request.getBanTypeId())
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

		LocalDateTime startTime = LocalDateTime.now();
		LocalDateTime endTime = startTime.plusDays(banType.getPeriod());

		UserBan savedUserBan = userBanRepository.save(UserBan.to(user, banType, startTime, endTime));
		return UserBanResponse.from(savedUserBan);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBanResponse> getAllBannedUsers() {
		return userBanRepository.findAll()
			.stream()
			.map(UserBanResponse::from)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBanResponse> getUserBans(Long userId) {
		return userBanRepository.findByUserUserId(userId)
			.stream()
			.map(UserBanResponse::from)
			.toList();
	}

	@Override
	@Transactional(readOnly = true)
	public UserBanResponse getActiveBan(Long userId) {
		return userBanRepository.findActiveBanByUserId(userId);
	}

	@Override
	@Transactional(readOnly = true)
	public UserBanResponse getActiveBanByUuid(String uuid) {
		return userBanRepository.findActiveBanByUuid(uuid);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isBannedByUuid(String uuid) {
		return userBanRepository.existsActiveBanByUuid(uuid);
	}

	@Override
	public void unbanUser(Long userBanId) {
		userBanRepository.deleteByUserBanId(userBanId);
	}
}