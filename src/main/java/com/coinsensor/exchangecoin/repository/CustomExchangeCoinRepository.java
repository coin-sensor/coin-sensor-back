package com.coinsensor.exchangecoin.repository;

import java.util.List;
import java.util.Optional;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;

public interface CustomExchangeCoinRepository {

	Optional<ExchangeCoin> findByExchangeNameAndTypeAndCoinTicker(String exchangeName, Exchange.Type type,
		String coinTicker);

	List<ExchangeCoin> getDetectableExchangeCoins(String exchangeName, Exchange.Type type);

	List<ExchangeCoin> findExchangeCoins();
}