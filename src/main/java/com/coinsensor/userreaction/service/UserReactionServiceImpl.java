package com.coinsensor.userreaction.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.entity.ReactionCount;
import com.coinsensor.reaction.repository.ReactionCountRepository;
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
	private final ReactionCountRepository reactionCountRepository;

	@Override
	public void toggleReaction(String userUuid, UserReactionRequest request) {
		User user = userRepository.findByUuid(userUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Reaction reaction = reactionRepository.findByName(request.getReactionName())
			.orElseThrow(() -> new CustomException(ErrorCode.REACTION_NOT_FOUND));

		// 기존 리액션 조회
		UserReaction userReaction = userReactionRepository.findByUserAndTargetTypeAndTargetId(user, request.getTargetType(),
			request.getTargetId()).orElse(null);

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
	}

	@Override
	public List<ReactionCountResponse> getReactionCounts(String targetType, Long targetId) {
		return reactionCountRepository.findByTargetTypeAndTargetId(targetType, targetId)
			.stream()
			.map(ReactionCountResponse::from)
			.toList();
	}

	@Override
	public List<CoinReactionResponse> getTopLikedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findTopLikedCoins(startTime, limit);
	}

	@Override
	public List<CoinReactionResponse> getTopDislikedCoins(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findTopDislikedCoins(startTime, limit);
	}

	@Override
	public List<ReactionTrendDataResponse> getReactionsTrendData(int days, int limit) {
		LocalDateTime startTime = LocalDateTime.now().minusDays(days);
		return userReactionRepository.findReactionsTrendData(startTime, limit);
	}

	private void updateReactionCount(String targetType, Long targetId, Reaction reaction, int delta) {
		ReactionCount reactionCount = reactionCountRepository
			.findByTargetTypeAndTargetIdAndReactionId(targetType, targetId, reaction.getReactionId())
			.orElse(ReactionCount.to(targetId, targetType, reaction, 0L));

		if (delta > 0) {
			reactionCount.incrementCount();
		} else {
			reactionCount.decrementCount();
		}

		reactionCountRepository.save(reactionCount);
	}
}