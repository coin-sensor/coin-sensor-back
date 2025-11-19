package com.coinsensor.favoritecoin.service;

import java.util.List;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;

public interface FavoriteCoinService {

	List<FavoriteCoinResponse> getFavoriteCoins(String uuid);

	void createOrDeleteFavoriteCoin(String uuid, Long exchangeCoinId);
}