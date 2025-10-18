package com.coinsensor.votetopic.repository;

import com.coinsensor.votetopic.entity.VoteTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteTopicRepository extends JpaRepository<VoteTopic, Long> {
}
