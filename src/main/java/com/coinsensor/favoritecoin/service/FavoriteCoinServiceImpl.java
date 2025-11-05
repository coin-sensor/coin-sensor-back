package com.coinsensor.favoritecoin.service;

import com.coinsensor.favoritecoin.entity.FavoriteCoin;
import com.coinsensor.favoritecoin.repository.FavoriteCoinRepository;
import com.coinsensor.favoritecoin.dto.response.FavoriteCoinResponse;
import com.coinsensor.user.repository.UserRepository;
import com.coinsensor.exchangecoin.repository.ExchangeCoinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteCoinServiceImpl implements FavoriteCoinService {
    
    private final FavoriteCoinRepository favoriteCoinRepository;
    private final UserRepository userRepository;
    private final ExchangeCoinRepository exchangeCoinRepository;
    
    @Override
    public List<FavoriteCoinResponse> getFavoriteCoins(String uuid) {
        var user = userRepository.findByUuid(uuid).orElseThrow();
        return favoriteCoinRepository.findByUser_UserId(user.getUserId())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    
    @Override
    @Transactional
    public void addFavoriteCoin(String uuid, Long exchangeCoinId) {
        var user = userRepository.findByUuid(uuid).orElseThrow();
        if (favoriteCoinRepository.findByUser_UserIdAndExchangeCoin_ExchangeCoinId(user.getUserId(), exchangeCoinId).isPresent()) {
            return;
        }
        
        var exchangeCoin = exchangeCoinRepository.findById(exchangeCoinId).orElseThrow();
        
        var favoriteCoin = FavoriteCoin.builder()
                .user(user)
                .exchangeCoin(exchangeCoin)
                .createdAt(LocalDateTime.now())
                .build();
        
        favoriteCoinRepository.save(favoriteCoin);
    }
    
    @Override
    @Transactional
    public void removeFavoriteCoin(String uuid, Long exchangeCoinId) {
        var user = userRepository.findByUuid(uuid).orElseThrow();
        favoriteCoinRepository.deleteByUser_UserIdAndExchangeCoin_ExchangeCoinId(user.getUserId(), exchangeCoinId);
    }
    
    private FavoriteCoinResponse toResponse(FavoriteCoin favoriteCoin) {
        return FavoriteCoinResponse.builder()
                .favoriteCoinId(favoriteCoin.getFavoriteCoinId())
                .exchangeCoinId(favoriteCoin.getExchangeCoin().getExchangeCoinId())
                .coinTicker(favoriteCoin.getExchangeCoin().getCoin().getCoinTicker())
                .baseAsset(favoriteCoin.getExchangeCoin().getCoin().getBaseAsset())
                .exchangeName(favoriteCoin.getExchangeCoin().getExchange().getName())
                .createdAt(favoriteCoin.getCreatedAt())
                .build();
    }
}