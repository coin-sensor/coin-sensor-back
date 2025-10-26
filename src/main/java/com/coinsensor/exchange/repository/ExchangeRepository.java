package com.coinsensor.exchange.repository;

import java.util.Optional;

import com.coinsensor.exchange.entity.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRepository extends JpaRepository<Exchange, Long> {

    boolean existsByName(String name);

    Optional<Exchange> findByName(String name);
    
    Optional<Exchange> findByNameAndExchangeType(String name, Exchange.ExchangeType exchangeType);
}
