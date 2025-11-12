package com.coinsensor.uservote.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.coinsensor.common.exception.CustomException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.uservote.dto.request.UserVoteRequest;
import com.coinsensor.uservote.dto.response.UserVoteResponse;
import com.coinsensor.uservote.entity.UserVote;
import com.coinsensor.uservote.repository.UserVoteRepository;
import com.coinsensor.voteoption.entity.VoteOption;
import com.coinsensor.voteoption.repository.VoteOptionRepository;
import com.coinsensor.votetopic.entity.VoteTopic;
import com.coinsensor.votetopic.repository.VoteTopicRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserVoteServiceImpl implements UserVoteService {

	private final UserVoteRepository userVoteRepository;
	private final VoteTopicRepository voteTopicRepository;
	private final VoteOptionRepository voteOptionRepository;
	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserVoteResponse vote(UserVoteRequest request) {
		VoteTopic voteTopic = voteTopicRepository.findById(request.getVoteTopicId())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_TOPIC_NOT_FOUND));
		VoteOption voteOption = voteOptionRepository.findById(request.getVoteOptionId())
			.orElseThrow(() -> new CustomException(ErrorCode.VOTE_TOPIC_NOT_FOUND));
		User user = userRepository.findById(request.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		UserVote userVote = UserVote.builder()
			.voteTopic(voteTopic)
			.voteOption(voteOption)
			.user(user)
			.createdAt(LocalDateTime.now())
			.build();

		return UserVoteResponse.from(userVoteRepository.save(userVote));
	}
}
