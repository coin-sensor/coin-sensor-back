package com.coinsensor.favoritecoin.service;

import java.util.List;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;

public interface FavoriteCoinService {

	List<FavoriteCoinResponse> getFavoriteCoins(String uuid);

	void addFavoriteCoin(String uuid, Long exchangeCoinId);

	void removeFavoriteCoin(String uuid, Long exchangeCoinId);
}