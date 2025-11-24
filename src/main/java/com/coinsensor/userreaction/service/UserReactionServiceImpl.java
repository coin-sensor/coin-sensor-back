package com.coinsensor.userreaction.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.repository.ReactionRepository;
import com.coinsensor.targettable.entity.TargetTable;
import com.coinsensor.targettable.repository.TargetTableRepository;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.userreaction.dto.request.UserReactionRequest;
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
	private final TargetTableRepository tableRepository;

	@Override
	public void toggleReaction(String userUuid, UserReactionRequest request) {
		User user = userRepository.findByUuid(userUuid)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Reaction reaction = reactionRepository.findByName(request.getReactionName())
			.orElseThrow(() -> new CustomException(ErrorCode.REACTION_NOT_FOUND));

		TargetTable targetTable = tableRepository.findByName(request.getTableName())
			.orElseThrow(() -> new CustomException(ErrorCode.TABLE_NOT_FOUND));

		// 기존 리액션 조회
		UserReaction userReaction = userReactionRepository.findByUserAndTargetTableAndTargetId(user, targetTable,
			request.getTargetId()).orElse(null);

		// 기존 리액션이 있는 경우
		if (userReaction != null) {
			if (userReaction.getReaction().equals(reaction)) {
				// 같은 리액션이면 리액션 삭제
				userReactionRepository.delete(userReaction);
			} else {
				// 다른 리액션이면 기존 리액션을 새 리액션으로 수정
				userReaction.updateReaction(reaction);
			}
		} else {
			// 새 리액션 추가
			userReaction = UserReaction.to(user, reaction, targetTable, request.getTargetId());
			userReactionRepository.save(userReaction);
		}

	}
}