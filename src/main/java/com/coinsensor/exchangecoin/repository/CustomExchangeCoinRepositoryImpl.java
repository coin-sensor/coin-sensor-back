package com.coinsensor.exchangecoin.repository;

import java.util.Optional;

import com.coinsensor.exchange.entity.Exchange;
import com.coinsensor.exchangecoin.entity.ExchangeCoin;
import com.coinsensor.exchangecoin.entity.QExchangeCoin;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomExchangeCoinRepositoryImpl implements CustomExchangeCoinRepository {
    
    private final JPAQueryFactory queryFactory;
    
    @Override
    public Optional<ExchangeCoin> findByExchangeNameAndTypeAndCoinTicker(String exchangeName, Exchange.Type type, String coinTicker) {
        QExchangeCoin exchangeCoin = QExchangeCoin.exchangeCoin;
        
        ExchangeCoin result = queryFactory
            .selectFrom(exchangeCoin)
            .join(exchangeCoin.exchange).fetchJoin()
            .join(exchangeCoin.coin).fetchJoin()
            .where(
                exchangeCoin.exchange.name.eq(exchangeName)
                .and(exchangeCoin.exchange.type.eq(type))
                .and(exchangeCoin.coin.coinTicker.eq(coinTicker))
            )
            .fetchOne();
            
        return Optional.ofNullable(result);
    }
}