package com.coinsensor.btcaireport.service;

import com.coinsensor.btcaireport.dto.response.BtcAiReportResponse;
import com.coinsensor.btcaireport.repository.BtcAiReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BtcAiReportServiceImpl implements BtcAiReportService {
    
    private final BtcAiReportRepository btcAiReportRepository;
    
    @Override
    public BtcAiReportResponse getLatestReport() {
        return btcAiReportRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(BtcAiReportResponse::from)
                .orElse(null);
    }
}
