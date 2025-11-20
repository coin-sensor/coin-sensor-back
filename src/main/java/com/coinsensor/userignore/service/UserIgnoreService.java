package com.coinsensor.userignore.service;

public interface UserIgnoreService {
    void ignoreUser(String uuid, Long targetUserId);
    void unignoreUser(String uuid, Long targetUserId);
}