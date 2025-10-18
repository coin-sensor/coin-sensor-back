package com.coinsensor.exchange.service;

import com.coinsensor.exchange.dto.response.ExchangeResponse;
import java.util.List;

public interface ExchangeService {
    List<ExchangeResponse> getAllExchanges();
}
