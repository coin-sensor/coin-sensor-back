package com.coinsensor.uservote.service;

import com.coinsensor.common.exception.BusinessException;
import com.coinsensor.common.exception.ErrorCode;
import com.coinsensor.user.entity.User;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.voteoption.entity.VoteOption;
import com.coinsensor.voteoption.repository.VoteOptionRepository;
import com.coinsensor.votetopic.entity.VoteTopic;
import com.coinsensor.votetopic.repository.VoteTopicRepository;
import com.coinsensor.uservote.dto.request.UserVoteRequest;
import com.coinsensor.uservote.dto.response.UserVoteResponse;
import com.coinsensor.uservote.entity.UserVote;
import com.coinsensor.uservote.repository.UserVoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

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
                .orElseThrow(() -> new BusinessException(ErrorCode.VOTE_TOPIC_NOT_FOUND));
        VoteOption voteOption = voteOptionRepository.findById(request.getVoteOptionId())
                .orElseThrow(() -> new BusinessException(ErrorCode.VOTE_TOPIC_NOT_FOUND));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
        
        UserVote userVote = UserVote.builder()
                .voteTopic(voteTopic)
                .voteOption(voteOption)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        
        return UserVoteResponse.from(userVoteRepository.save(userVote));
    }
}
