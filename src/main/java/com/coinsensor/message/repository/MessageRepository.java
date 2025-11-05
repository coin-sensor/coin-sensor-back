package com.coinsensor.message.repository;

import com.coinsensor.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT cm FROM Message cm WHERE cm.chatRoom.roomId = :roomId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<Message> findRecentMessagesByRoomId(@Param("roomId") Long roomId, @Param("limit") int limit);
    
    @Query("SELECT cm FROM Message cm WHERE cm.chatRoom.roomId = :roomId AND cm.messageId < :lastMessageId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<Message> findMessagesBefore(@Param("roomId") Long roomId, @Param("lastMessageId") Long lastMessageId, @Param("limit") int limit);
}
