package com.coinsensor.chatban.repository;

import com.coinsensor.chatban.entity.ChatBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBanRepository extends JpaRepository<ChatBan, Long> {
}
