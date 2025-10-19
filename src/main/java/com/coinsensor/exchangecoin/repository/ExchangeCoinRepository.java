package com.coinsensor.exchangecoin.repository;

import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeCoinRepository extends JpaRepository<ExchangeCoin, Long> {
    boolean existsByExchange_ExchangeIdAndCoin_CoinTickerAndExchangeType(
            Long exchangeId, String coinTicker, ExchangeCoin.ExchangeType exchangeType);
    
    List<ExchangeCoin> findByExchange_ExchangeIdAndExchangeType(
            Long exchangeId, ExchangeCoin.ExchangeType exchangeType);
}
