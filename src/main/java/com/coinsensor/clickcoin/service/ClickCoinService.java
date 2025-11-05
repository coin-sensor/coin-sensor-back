package com.coinsensor.clickcoin.service;

public interface ClickCoinService {
    void incrementClickCount(String uuid, Long exchangeCoinId);
}