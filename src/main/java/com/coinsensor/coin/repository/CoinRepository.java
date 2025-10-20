package com.coinsensor.coin.repository;

import com.coinsensor.coin.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByCoinTicker(String coinTicker);
    
    @Query("SELECT DISTINCT c FROM Coin c JOIN ExchangeCoin ec ON c.coinId = ec.coin.coinId WHERE ec.exchangeType = 'spot' AND ec.isActive = true")
    List<Coin> findSpotCoins();
    
    @Query("SELECT DISTINCT c FROM Coin c JOIN ExchangeCoin ec ON c.coinId = ec.coin.coinId WHERE ec.exchangeType = 'future' AND ec.isActive = true")
    List<Coin> findFutureCoins();
}
