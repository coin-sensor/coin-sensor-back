package com.coinsensor.kimchipremium.service;

import com.coinsensor.kimchipremium.dto.response.KimchiPremiumResponse;
import com.coinsensor.kimchipremium.repository.KimchiPremiumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KimchiPremiumServiceImpl implements KimchiPremiumService {
    
    private final KimchiPremiumRepository kimchiPremiumRepository;
    
    @Override
    public KimchiPremiumResponse getLatest() {
        return kimchiPremiumRepository.findAll().stream()
                .reduce((first, second) -> second)
                .map(KimchiPremiumResponse::from)
                .orElse(null);
    }
}
