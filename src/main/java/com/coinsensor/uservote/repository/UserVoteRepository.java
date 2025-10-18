package com.coinsensor.uservote.repository;

import com.coinsensor.uservote.entity.UserVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoteRepository extends JpaRepository<UserVote, Long> {
}
