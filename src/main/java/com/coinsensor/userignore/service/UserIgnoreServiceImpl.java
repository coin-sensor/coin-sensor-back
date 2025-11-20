package com.coinsensor.userignore.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.userignore.entity.UserIgnore;
import com.coinsensor.userignore.repository.UserIgnoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserIgnoreServiceImpl implements UserIgnoreService {

	private final UserIgnoreRepository userIgnoreRepository;
	private final UserRepository userRepository;

	@Override
	public void ignoreUser(String uuid, Long targetUserId) {
		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		User targetUser = userRepository.findByUserId(targetUserId)
			.orElseThrow(() -> new CustomException(ErrorCode.TARGET_USER_NOT_FOUND));

		userIgnoreRepository.save(UserIgnore.to(user, targetUser));
	}

	@Override
	public void unignoreUser(String uuid, Long targetUserId) {
		userIgnoreRepository.deleteByUuidAndTargetUserId(uuid, targetUserId);
	}

}