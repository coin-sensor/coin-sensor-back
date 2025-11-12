package com.coinsensor.user.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.dto.response.UserInfoResponse;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	@Cacheable(value = "users", key = "#uuid")
	public User getUserByUuid(String uuid) {
		return userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	@Override
	public UserInfoResponse getUserInfo(String uuid) {
		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		return UserInfoResponse.from(user);
	}

	@Override
	@Transactional
	public UserInfoResponse updateNickname(String uuid, String nickname) {
		User user = userRepository.findByUuid(uuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		user.updateNickname(nickname);
		return UserInfoResponse.from(user);
	}
}