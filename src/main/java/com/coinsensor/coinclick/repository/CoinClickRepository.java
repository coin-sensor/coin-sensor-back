package com.coinsensor.coinclick.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coinsensor.coinclick.entity.CoinClick;

public interface CoinClickRepository extends JpaRepository<CoinClick, Long>, CustomCoinClickRepository {
}