package com.coinsensor.ohlcvs.service;

import com.coinsensor.ohlcvs.dto.response.OhlcvResponse;
import java.util.List;

public interface OhlcvService {
    
    List<OhlcvResponse> getOhlcvByExchangeCoinId(Long exchangeCoinId);
    
    long cleanupOldData(int years);
}