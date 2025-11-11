package com.coinsensor.clickcoin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.clickcoin.repository.ClickCoinRepository;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ClickCoinServiceImpl implements ClickCoinService {

	private final ClickCoinRepository clickCoinRepository;
	private final UserRepository userRepository;
	private final DetectedCoinRepository detectedCoinRepository;

}