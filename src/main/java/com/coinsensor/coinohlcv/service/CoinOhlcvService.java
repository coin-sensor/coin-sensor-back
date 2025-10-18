package com.coinsensor.coinohlcv.service;

import com.coinsensor.coinohlcv.dto.response.CoinOhlcvResponse;
import java.util.List;

public interface CoinOhlcvService {
    List<CoinOhlcvResponse> getOhlcvByCoinId(Long coinId);
}
