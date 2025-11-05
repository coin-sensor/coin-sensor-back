package com.coinsensor.favoritecoin.service;

import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import java.util.List;

public interface FavoriteCoinService {
    List<FavoriteCoinResponse> getFavoriteCoins(String uuid);
    void addFavoriteCoin(String uuid, Long exchangeCoinId);
    void removeFavoriteCoin(String uuid, Long exchangeCoinId);
}