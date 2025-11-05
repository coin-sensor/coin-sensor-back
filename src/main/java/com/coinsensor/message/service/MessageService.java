package com.coinsensor.message.service;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import java.util.List;

public interface MessageService {
    MessageResponse saveMessage(MessageRequest request);
    List<MessageResponse> getRecentMessages(Long roomId, int limit);
    List<MessageResponse> getMessagesByRoomId(Long roomId);
    List<MessageResponse> getMessagesBefore(Long roomId, Long lastMessageId, int limit);
    void deleteMessage(Long messageId);
}