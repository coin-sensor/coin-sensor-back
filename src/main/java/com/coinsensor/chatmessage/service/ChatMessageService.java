package com.coinsensor.chatmessage.service;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse saveMessage(ChatMessageRequest request);
    List<ChatMessageResponse> getRecentMessages(Long roomId, int limit);
    List<ChatMessageResponse> getMessagesByRoomId(Long roomId);
    List<ChatMessageResponse> getMessagesBefore(Long roomId, Long lastMessageId, int limit);
    void deleteMessage(Long messageId);
}