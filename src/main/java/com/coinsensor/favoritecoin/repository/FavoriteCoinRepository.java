package com.coinsensor.favoritecoin.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.favoritecoin.entity.FavoriteCoin;

public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoin, Long>, CustomFavoriteCoinRepository {
	Optional<FavoriteCoin> findByUser_UserIdAndExchangeCoin_ExchangeCoinId(Long userId, Long exchangeCoinId);

	void deleteByUser_UserIdAndExchangeCoin_ExchangeCoinId(Long userId, Long exchangeCoinId);
}