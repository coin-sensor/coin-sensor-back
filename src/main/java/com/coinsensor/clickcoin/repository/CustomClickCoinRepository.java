package com.coinsensor.clickcoin.repository;

import java.util.Optional;

import com.coinsensor.clickcoin.entity.ClickCoin;

public interface CustomClickCoinRepository {

	Optional<ClickCoin> findByUuidAndDetectedCoinId(String uuid, Long detectedCoinId);
}
