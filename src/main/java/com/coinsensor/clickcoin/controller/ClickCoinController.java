package com.coinsensor.clickcoin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coinsensor.clickcoin.service.ClickCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clickCoins")
@RequiredArgsConstructor
public class ClickCoinController {

	private final ClickCoinService clickCoinService;

}