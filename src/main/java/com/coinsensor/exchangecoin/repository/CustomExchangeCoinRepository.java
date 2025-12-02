package com.coinsensor.exchangecoin.repository;

import java.util.Optional;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

public interface CustomExchangeCoinRepository {
    
    Optional<ExchangeCoin> findByExchangeNameAndTypeAndCoinTicker(String exchangeName, Exchange.Type type, String coinTicker);
}