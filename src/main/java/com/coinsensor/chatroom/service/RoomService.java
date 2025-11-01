package com.coinsensor.chatroom.service;

import com.coinsensor.chatroom.dto.request.RoomRequest;
import com.coinsensor.chatroom.dto.response.RoomResponse;
import java.util.List;

public interface RoomService {
    RoomResponse createRoom(RoomRequest request);
    List<RoomResponse> getAllRooms();
    RoomResponse getRoomById(Long roomId);
    RoomResponse updateRoom(Long roomId, RoomRequest request);
    void deleteRoom(Long roomId);
}