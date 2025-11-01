package com.coinsensor.chatmessage.repository;

import com.coinsensor.chatmessage.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.roomId = :roomId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<ChatMessage> findRecentMessagesByRoomId(@Param("roomId") Long roomId, @Param("limit") int limit);
    
    @Query("SELECT cm FROM ChatMessage cm WHERE cm.chatRoom.roomId = :roomId AND cm.messageId < :lastMessageId AND cm.isDeleted = false ORDER BY cm.createdAt DESC LIMIT :limit")
    List<ChatMessage> findMessagesBefore(@Param("roomId") Long roomId, @Param("lastMessageId") Long lastMessageId, @Param("limit") int limit);
}
