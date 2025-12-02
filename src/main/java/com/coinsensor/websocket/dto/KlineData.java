package com.coinsensor.websocket.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KlineData {
    @JsonProperty("e")
    private String eventType;
    
    @JsonProperty("E")
    private Long eventTime;
    
    @JsonProperty("s")
    private String symbol;
    
    @JsonProperty("k")
    private KlineInfo kline;
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KlineInfo {
        @JsonProperty("t")
        private Long startTime;
        
        @JsonProperty("T")
        private Long closeTime;
        
        @JsonProperty("s")
        private String symbol;
        
        @JsonProperty("i")
        private String interval;
        
        @JsonProperty("o")
        private String open;
        
        @JsonProperty("c")
        private String close;
        
        @JsonProperty("h")
        private String high;
        
        @JsonProperty("l")
        private String low;
        
        @JsonProperty("v")
        private String volume;
        
        @JsonProperty("q")
        private String quoteVolume;
        
        @JsonProperty("x")
        private Boolean isClosed;
        
        @JsonProperty("f")
        private Long firstTradeId;
        
        @JsonProperty("L")
        private Long lastTradeId;
        
        @JsonProperty("n")
        private Long tradeCount;
        
        @JsonProperty("V")
        private String takerBuyBaseAssetVolume;
        
        @JsonProperty("Q")
        private String takerBuyQuoteAssetVolume;
        
        public BigDecimal getOpenPrice() {
            return new BigDecimal(open);
        }
        
        public BigDecimal getClosePrice() {
            return new BigDecimal(close);
        }
        
        public BigDecimal getHighPrice() {
            return new BigDecimal(high);
        }
        
        public BigDecimal getLowPrice() {
            return new BigDecimal(low);
        }
        
        public BigDecimal getVolumeAmount() {
            return new BigDecimal(volume);
        }
        
        public LocalDateTime getCandleTime() {
            return LocalDateTime.ofEpochSecond(startTime / 1000, 0, java.time.ZoneOffset.UTC);
        }
    }
}