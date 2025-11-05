package com.coinsensor.reaction.service;

import com.coinsensor.reaction.entity.Reaction;
import com.coinsensor.reaction.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService implements CommandLineRunner {
    
    private final ReactionRepository reactionRepository;
    
    @Override
    public void run(String... args) {
        initializeReactions();
    }
    
    private void initializeReactions() {
        if (reactionRepository.count() == 0) {
            reactionRepository.save(Reaction.builder().type("like").build());
            reactionRepository.save(Reaction.builder().type("dislike").build());
            reactionRepository.save(Reaction.builder().type("love").build());
            reactionRepository.save(Reaction.builder().type("angry").build());
        }
    }
}