package com.coinsensor.userignore.repository;

public interface CustomUserIgnoreRepository {
    void deleteByUuidAndTargetUserId(String uuid, Long targetUserId);
}