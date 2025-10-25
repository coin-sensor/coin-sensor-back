package com.coinsensor.common.config;

import com.coinsensor.exchangecoin.entity.ExchangeCoin.ExchangeType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToExchangeTypeConverter implements Converter<String, ExchangeType> {
    
    @Override
    public ExchangeType convert(String source) {
        return ExchangeType.valueOf(source.toLowerCase());
    }
}