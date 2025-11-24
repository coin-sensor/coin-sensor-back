package com.coinsensor.detectedcoin.service;

import static com.coinsensor.common.exception.ErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.clickcoin.entity.ClickCoin;
import com.coinsensor.clickcoin.repository.ClickCoinRepository;
import com.coinsensor.common.exception.CustomException;
import com.coinsensor.detectedcoin.dto.response.DetectedCoinResponse;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.service.UserReactionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DetectedCoinServiceImpl implements DetectedCoinService {
	private final DetectedCoinRepository detectedCoinRepository;
	private final ClickCoinRepository clickCoinRepository;
	private final UserRepository userRepository;
	private final UserReactionService userReactionService;

	@Override
	public List<DetectedCoinResponse> getAbnormalCoins() {
		return detectedCoinRepository.findAll().stream()
			.map(this::mapToResponseWithReactions)
			.toList();
	}

	@Override
	public List<DetectedCoinResponse> getVolatileCoins() {
		return detectedCoinRepository.findAll().stream()
			.sorted((a, b) -> b.getChangeX().compareTo(a.getChangeX()))
			.limit(20)
			.map(this::mapToResponseWithReactions)
			.toList();
	}

	@Override
	public List<DetectedCoinResponse> getDetectedCoinsByTimeAndType(String exchangeName, String exchangeType) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime startTime = now.withSecond(0).withNano(0);
		LocalDateTime endTime = startTime.plusMinutes(1);

		Exchange.Type type = Exchange.Type.valueOf(exchangeType);
		return detectedCoinRepository.findByExchangeNameAndTypeAndTime(exchangeName, type, startTime, endTime)
			.stream()
			.map(this::mapToResponseWithReactions)
			.toList();
	}

	@Override
	public void viewDetectedCoin(String uuid, Long detectedCoinId) {
		ClickCoin clickCoin = clickCoinRepository.findByUuidAndDetectedCoinId(uuid, detectedCoinId).orElse(null);

		if (clickCoin == null) {
			User user = userRepository.findByUuid(uuid).orElseThrow(() -> new CustomException(USER_NOT_FOUND));
			DetectedCoin detectedCoin = detectedCoinRepository.findById(detectedCoinId)
				.orElseThrow(() -> new CustomException(DETECTED_COIN_NOT_FOUND));
			clickCoinRepository.save(new ClickCoin(user, detectedCoin));
			detectedCoin.incrementViewCount();
		} else {
			clickCoin.reClick();
		}
	}

	private DetectedCoinResponse mapToResponseWithReactions(DetectedCoin detectedCoin) {
		List<ReactionCountResponse> reactionCounts = userReactionService
			.getReactionCounts("detected_coins", detectedCoin.getDetectedCoinId());
		return DetectedCoinResponse.of(detectedCoin, reactionCounts);
	}
}
