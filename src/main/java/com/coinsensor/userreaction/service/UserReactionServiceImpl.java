package com.coinsensor.userreaction.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.detectedcoin.entity.DetectedCoin;
import com.coinsensor.detectedcoin.repository.DetectedCoinRepository;
import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.repository.ReactionRepository;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.userreaction.dto.response.CoinReactionResponse;
import com.coinsensor.userreaction.dto.response.ReactionCountResponse;
import com.coinsensor.userreaction.dto.response.ReactionTrendDataResponse;
import com.coinsensor.userreaction.entity.UserReaction;
import com.coinsensor.userreaction.repository.UserReactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserReactionServiceImpl implements UserReactionService {

	private final UserReactionRepository userReactionRepository;
	private final UserRepository userRepository;
	private final ReactionRepository reactionRepository;
	private final DetectedCoinRepository detectedCoinRepository;

	@Override
	public List<ReactionCountResponse> toggleReaction(String userUuid, UserReactionRequest request) {
		User user = userRepository.findByUuid(userUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Reaction reaction = reactionRepository.findByName(request.getReactionName())
			.orElseThrow(() -> new CustomException(ErrorCode.REACTION_NOT_FOUND));

		// 기존 리액션 조회
		UserReaction userReaction = userReactionRepository.findByUserAndTargetTypeAndTargetId(user,
			request.getTargetType(), request.getTargetId()).orElse(null);

		// 기존 리액션이 있는 경우
		if (userReaction != null) {
			if (userReaction.getReaction().equals(reaction)) {
				// 같은 리액션이면 리액션 삭제
				userReactionRepository.delete(userReaction);
				updateReactionCount(request.getTargetType(), request.getTargetId(), reaction, -1);
			} else {
				// 다른 리액션이면 기존 리액션을 새 리액션으로 수정
				Reaction oldReaction = userReaction.getReaction();
				userReaction.updateReaction(reaction);
				updateReactionCount(request.getTargetType(), request.getTargetId(), oldReaction, -1);
				updateReactionCount(request.getTargetType(), request.getTargetId(), reaction, 1);
			}
		} else {
			// 새 리액션 추가
			userReaction = UserReaction.to(user, reaction, request.getTargetType(), request.getTargetId());
			userReactionRepository.save(userReaction);
			updateReactionCount(request.getTargetType(), request.getTargetId(), reaction, 1);
		}

		// 업데이트된 리액션 카운트 반환
		return getReactionCounts(request.getTargetType(), request.getTargetId());
	}

	@Override
	public List<ReactionCountResponse> getReactionCounts(String targetType, Long targetId) {
		if ("detected_coins".equals(targetType)) {
			// detected_coins의 경우 엔티티 필드에서 직접 조회
			DetectedCoin detectedCoin = detectedCoinRepository.findById(targetId)
				.orElseThrow(() -> new CustomException(ErrorCode.DETECTED_COIN_NOT_FOUND));

			return List.of(
				new ReactionCountResponse("like", detectedCoin.getLikeCount()),
				new ReactionCountResponse("dislike", detectedCoin.getDislikeCount())
			);
		} else {
			// TODO: 다른 테이블도 필드 추가 방식으로 변경 예정
			return List.of();
		}
	}

	@Override
	@Cacheable(value = "topLikedCoins", key = "#days + '_' + #limit", cacheManager = "cacheManager")
	public List<CoinReactionResponse> getTopLikedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findTopLikedCoins(startTime, limit);
	}

	@Override
	@Cacheable(value = "topDislikedCoins", key = "#days + '_' + #limit", cacheManager = "cacheManager")
	public List<CoinReactionResponse> getTopDislikedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findTopDislikedCoins(startTime, limit);
	}

	@Override
	@Cacheable(value = "likeTrendData", key = "#days + '_' + #limit", cacheManager = "cacheManager")
	public List<ReactionTrendDataResponse> getLikeTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findLikeTrendData(startTime, limit);
	}

	@Override
	@Cacheable(value = "dislikeTrendData", key = "#days + '_' + #limit", cacheManager = "cacheManager")
	public List<ReactionTrendDataResponse> getDislikeTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findDislikeTrendData(startTime, limit);
	}

	private void updateReactionCount(String targetType, Long targetId, Reaction reaction, int delta) {
		// detected_coins 테이블의 경우 엔티티 필드 직접 업데이트
		if ("detected_coins".equals(targetType)) {
			DetectedCoin detectedCoin = detectedCoinRepository.findById(targetId)
				.orElseThrow(() -> new CustomException(ErrorCode.DETECTED_COIN_NOT_FOUND));

			if ("like".equals(reaction.getName())) {
				if (delta > 0) {
					detectedCoin.incrementLikeCount();
				} else {
					detectedCoin.decrementLikeCount();
				}
			} else if ("dislike".equals(reaction.getName())) {
				if (delta > 0) {
					detectedCoin.incrementDislikeCount();
				} else {
					detectedCoin.decrementDislikeCount();
				}
			}
		}
		// TODO: 다른 테이블도 필드 추가 방식으로 변경 예정
	}
}