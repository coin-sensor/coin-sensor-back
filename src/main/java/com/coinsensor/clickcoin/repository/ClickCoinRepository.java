package com.coinsensor.clickcoin.repository;

import com.coinsensor.clickcoin.entity.ClickCoin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ClickCoinRepository extends JpaRepository<ClickCoin, Long>,  CustomClickCoinRepository{
}