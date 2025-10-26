package com.coinsensor.common.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.coinsensor.exchange.entity.Exchange;

@Component
public class StringToExchangeTypeConverter implements Converter<String, Exchange.ExchangeType> {
    
    @Override
    public Exchange.ExchangeType convert(String source) {
        return Exchange.ExchangeType.valueOf(source.toLowerCase());
    }
}