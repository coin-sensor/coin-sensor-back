package com.coinsensor.detectedcoin.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.coinclick.entity.CoinClick;
import com.coinsensor.coinclick.repository.CoinClickRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DetectedCoinServiceImpl implements DetectedCoinService {
	private final DetectedCoinRepository detectedCoinRepository;
	private final CoinClickRepository coinClickRepository;
	private final UserRepository userRepository;

	@Override
	public List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startTime = now.withSecond(0).withNano(0);
		LocalDateTime endTime = startTime.plusMinutes(1);

		Exchange.Type type = Exchange.Type.valueOf(exchangeType);
		return detectedCoinRepository.findByExchangeNameAndTypeAndTime(exchangeName, type, startTime, endTime)
			.stream()
			.map(DetectedCoinResponse::from)
			.toList();
	}

	@Override
	public Long viewDetectedCoin(String uuid, Long detectedCoinId) {
		CoinClick coinClick = coinClickRepository.findByUuidAndDetectedCoinId(uuid, detectedCoinId).orElse(null);
		DetectedCoin detectedCoin = detectedCoinRepository.findById(detectedCoinId)
			.orElseThrow(() -> new CustomException(DETECTED_COIN_NOT_FOUND));

		if (coinClick == null) {
			User user = userRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
			coinClickRepository.save(new CoinClick(user, detectedCoin));
			detectedCoin.incrementViewCount();
		} else {
			coinClick.reClick();
		}

		return detectedCoin.getViewCount();
	}

}
