package com.coinsensor.reaction.repository;

import com.coinsensor.reaction.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    Optional<Reaction> findByName(String name);
}