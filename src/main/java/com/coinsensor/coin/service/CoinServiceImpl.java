package com.coinsensor.coin.service;

import com.coinsensor.coin.dto.response.CoinResponse;
import com.coinsensor.coin.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CoinServiceImpl implements CoinService {
    
    private final CoinRepository coinRepository;
    
    @Override
    public List<CoinResponse> getAllCoins() {
        return coinRepository.findAll().stream()
                .map(CoinResponse::from)
                .toList();
    }
    
    @Override
    public CoinResponse getCoinById(Long coinId) {
        return coinRepository.findById(coinId)
                .map(CoinResponse::from)
                .orElseThrow(() -> new RuntimeException("Coin not found"));
    }
}
