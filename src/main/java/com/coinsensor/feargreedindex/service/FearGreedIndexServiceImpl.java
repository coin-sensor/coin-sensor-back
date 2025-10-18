package com.coinsensor.feargreedindex.service;

import com.coinsensor.feargreedindex.dto.response.FearGreedIndexResponse;
import com.coinsensor.feargreedindex.repository.FearGreedIndexRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FearGreedIndexServiceImpl implements FearGreedIndexService {
    
    private final FearGreedIndexRepository fearGreedIndexRepository;
    
    @Override
    public FearGreedIndexResponse getLatest() {
        return fearGreedIndexRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(FearGreedIndexResponse::from)
                .orElse(null);
    }
}
