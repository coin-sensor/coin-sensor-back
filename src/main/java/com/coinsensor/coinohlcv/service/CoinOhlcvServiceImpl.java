package com.coinsensor.coinohlcv.service;

import com.coinsensor.coinohlcv.dto.response.CoinOhlcvResponse;
import com.coinsensor.coinohlcv.repository.CoinOhlcvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinOhlcvServiceImpl implements CoinOhlcvService {
    
    private final CoinOhlcvRepository coinOhlcvRepository;
    
    @Override
    public List<CoinOhlcvResponse> getOhlcvByCoinId(Long coinId) {
        return coinOhlcvRepository.findAll().stream()
                .filter(ohlcv -> ohlcv.getCoin().getCoinId().equals(coinId))
                .map(CoinOhlcvResponse::from)
                .toList();
    }
}
