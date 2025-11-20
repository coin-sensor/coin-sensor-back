package com.coinsensor.userban.repository;

public interface CustomUserBanRepository {

    void deleteByUuidAndTargetUserId(String uuid, Long targetUserId);
}