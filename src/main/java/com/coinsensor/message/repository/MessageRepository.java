package com.coinsensor.message.repository;

import com.coinsensor.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT cm FROM Message cm WHERE cm.channel.channelId = :channelId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<Message> findRecentMessagesByChannelId(@Param("channelId") Long channelId, @Param("limit") int limit);
    
    @Query("SELECT cm FROM Message cm WHERE cm.channel.channelId = :channelId AND cm.messageId < :lastMessageId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<Message> findMessagesBefore(@Param("channelId") Long channelId, @Param("lastMessageId") Long lastMessageId, @Param("limit") int limit);
}
