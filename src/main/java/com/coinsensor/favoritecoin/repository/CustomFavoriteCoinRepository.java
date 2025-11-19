package com.coinsensor.favoritecoin.repository;

import java.util.List;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;

public interface CustomFavoriteCoinRepository {
	List<FavoriteCoinResponse> findAllByUuid(String uuid);

	boolean existsByUuidAndExchangeCoinId(String uuid, Long exchangeCoinId);

	void deleteByUuidAndExchangeCoinId(String uuid, Long exchangeCoinId);
}