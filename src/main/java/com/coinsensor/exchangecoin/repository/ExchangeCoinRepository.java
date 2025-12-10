package com.coinsensor.exchangecoin.repository;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ExchangeCoinRepository extends JpaRepository<ExchangeCoin, Long>, CustomExchangeCoinRepository {
    boolean existsByExchange_ExchangeIdAndCoin_CoinTicker(Long exchangeId, String coinTicker);
    List<ExchangeCoin> findByExchange_ExchangeId(Long exchangeId);
    

}
