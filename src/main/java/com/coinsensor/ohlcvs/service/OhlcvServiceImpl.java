package com.coinsensor.ohlcvs.service;

import com.coinsensor.ohlcvs.dto.response.OhlcvResponse;
import com.coinsensor.ohlcvs.repository.OhlcvRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OhlcvServiceImpl implements OhlcvService {
    
    private final OhlcvRepository ohlcvRepository;
    
    @Override
    public List<OhlcvResponse> getOhlcvByExchangeCoinId(Long exchangeCoinId) {
        return ohlcvRepository.findAll().stream()
                .filter(ohlcv -> ohlcv.getExchangeCoin().getExchangeCoinId().equals(exchangeCoinId))
                .map(OhlcvResponse::from)
                .toList();
    }
    
    @Override
    @Transactional
    public long cleanupOldData(int years) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusYears(years);
        return ohlcvRepository.deleteByCreatedAtBefore(cutoffDate);
    }
}