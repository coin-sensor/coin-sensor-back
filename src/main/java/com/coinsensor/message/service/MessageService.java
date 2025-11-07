package com.coinsensor.message.service;

import com.coinsensor.message.dto.request.MessageRequest;
import com.coinsensor.message.dto.response.MessageResponse;
import java.util.List;

public interface MessageService {
    MessageResponse saveMessage(MessageRequest request);
    List<MessageResponse> getRecentMessages(Long channelId, int limit);
    List<MessageResponse> getMessagesByChannelId(Long channelId);
    List<MessageResponse> getMessagesBefore(Long channelId, Long lastMessageId, int limit);
    void deleteMessage(Long messageId);
}