package com.coinsensor.chatmessage.service;

import com.coinsensor.chatmessage.dto.request.ChatMessageRequest;
import com.coinsensor.chatmessage.dto.response.ChatMessageResponse;
import java.util.List;

public interface ChatMessageService {
    List<ChatMessageResponse> getMessagesByRoomId(Long roomId);
    ChatMessageResponse sendMessage(ChatMessageRequest request);
}
