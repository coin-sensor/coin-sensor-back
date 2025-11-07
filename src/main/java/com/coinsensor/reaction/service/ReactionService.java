package com.coinsensor.reaction.service;

import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {
    
    private final ReactionRepository reactionRepository;
}