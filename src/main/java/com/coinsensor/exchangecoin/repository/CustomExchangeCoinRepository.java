package com.coinsensor.exchangecoin.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

public interface CustomExchangeCoinRepository {
    
    Optional<ExchangeCoin> findByExchangeNameAndTypeAndCoinTicker(String exchangeName, Exchange.Type type, String coinTicker);
    
    List<ExchangeCoin> findByExchangeNameAndTypeAndIsActive(String exchangeName, Exchange.Type type, Boolean isActive);

    List<ExchangeCoin> findExchangeCoins();
}