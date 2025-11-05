package com.coinsensor.analysis.service;

import com.coinsensor.analysis.dto.response.AnalysisResponse;
import com.coinsensor.analysis.repository.AnalysisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalysisServiceImpl implements AnalysisService {
    
    private final AnalysisRepository btcAiReportRepository;
    
    @Override
    public AnalysisResponse getLatestReport() {
        return btcAiReportRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(AnalysisResponse::from)
                .orElse(null);
    }
}
