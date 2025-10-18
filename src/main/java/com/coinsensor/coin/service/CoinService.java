package com.coinsensor.coin.service;

import com.coinsensor.coin.dto.response.CoinResponse;
import java.util.List;

public interface CoinService {
    List<CoinResponse> getAllCoins();
    CoinResponse getCoinById(Long coinId);
}
