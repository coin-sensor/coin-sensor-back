package com.coinsensor.userreaction.service;

import com.coinsensor.user.entity.User;
import com.coinsensor.userreaction.entity.UserReaction;
import com.coinsensor.userreaction.repository.UserReactionRepository;
import com.coinsensor.userreaction.dto.request.UserReactionRequest;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserReactionServiceImpl implements UserReactionService {
    
    private final UserReactionRepository userReactionRepository;
    private final UserRepository userRepository;
    private final ReactionRepository reactionRepository;
    

}