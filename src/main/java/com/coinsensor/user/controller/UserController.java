package com.coinsensor.user.controller;

import com.coinsensor.user.dto.request.UpdateNicknameRequest;
import com.coinsensor.user.dto.response.UserInfoResponse;
import com.coinsensor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestHeader("uuid") String uuid) {
        return ResponseEntity.ok(userService.getUserInfo(uuid));
    }
    
    @PutMapping("/nickname")
    public ResponseEntity<UserInfoResponse> updateNickname(
            @RequestHeader("uuid") String uuid,
            @RequestBody UpdateNicknameRequest request) {
        return ResponseEntity.ok(userService.updateNickname(uuid, request.getNickname()));
    }
}