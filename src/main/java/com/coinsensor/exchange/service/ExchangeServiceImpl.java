package com.coinsensor.exchange.service;

import com.coinsensor.exchange.dto.response.ExchangeResponse;
import com.coinsensor.exchange.repository.ExchangeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExchangeServiceImpl implements ExchangeService {
    
    private final ExchangeRepository exchangeRepository;
    
    @Override
    public List<ExchangeResponse> getAllExchanges() {
        return exchangeRepository.findAll().stream()
                .map(ExchangeResponse::from)
                .toList();
    }
}
