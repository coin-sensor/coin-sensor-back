package com.coinsensor.chatroom.service;

import com.coinsensor.chatroom.dto.request.ChatRoomRequest;
import com.coinsensor.chatroom.dto.response.ChatRoomResponse;
import java.util.List;

public interface ChatRoomService {
    ChatRoomResponse createRoom(ChatRoomRequest request);
    List<ChatRoomResponse> getAllRooms();
    ChatRoomResponse getRoomById(Long roomId);
    ChatRoomResponse updateRoom(Long roomId, ChatRoomRequest request);
    void deleteRoom(Long roomId);
}