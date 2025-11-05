package com.coinsensor.clickcoin.service;

import com.coinsensor.clickcoin.entity.ClickCoin;
import com.coinsensor.clickcoin.repository.ClickCoinRepository;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ClickCoinServiceImpl implements ClickCoinService {
    
    private final ClickCoinRepository clickCoinRepository;
    private final UserRepository userRepository;
    private final ExchangeCoinRepository exchangeCoinRepository;
    
    @Override
    @Transactional
    public void incrementClickCount(String uuid, Long exchangeCoinId) {
        var user = userRepository.findByUuid(uuid).orElseThrow();
        var existingClick = clickCoinRepository.findByUser_UserIdAndExchangeCoin_ExchangeCoinId(user.getUserId(), exchangeCoinId);
        
        if (existingClick.isPresent()) {
            var clickCoin = existingClick.get();
            var updatedClick = ClickCoin.builder()
                    .clickCoinId(clickCoin.getClickCoinId())
                    .user(clickCoin.getUser())
                    .exchangeCoin(clickCoin.getExchangeCoin())
                    .count(clickCoin.getCount() + 1)
                    .createdAt(clickCoin.getCreatedAt())
                    .build();
            clickCoinRepository.save(updatedClick);
        } else {
            var exchangeCoin = exchangeCoinRepository.findById(exchangeCoinId).orElseThrow();
            
            var newClick = ClickCoin.builder()
                    .user(user)
                    .exchangeCoin(exchangeCoin)
                    .count(1L)
                    .createdAt(LocalDateTime.now())
                    .build();
            clickCoinRepository.save(newClick);
        }
    }
}