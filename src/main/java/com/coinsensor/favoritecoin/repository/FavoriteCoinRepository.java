package com.coinsensor.favoritecoin.repository;

import com.coinsensor.favoritecoin.entity.FavoriteCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoin, Long> {
    List<FavoriteCoin> findByUser_UserId(Long userId);
    Optional<FavoriteCoin> findByUser_UserIdAndExchangeCoin_ExchangeCoinId(Long userId, Long exchangeCoinId);
    void deleteByUser_UserIdAndExchangeCoin_ExchangeCoinId(Long userId, Long exchangeCoinId);
}