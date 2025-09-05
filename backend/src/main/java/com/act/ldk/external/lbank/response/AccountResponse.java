package com.act.ldk.external.lbank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class AccountResponse {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("ts")
    private Long timestamp;
    
    @JsonProperty("data")
    private Map<String, CoinBalance> data;
    
    @Data
    public static class CoinBalance {
        @JsonProperty("free")
        private BigDecimal free;
        
        @JsonProperty("freeze")
        private BigDecimal freeze;
    }
    
    public BigDecimal getAvailableBalance(String coin) {
        if (data != null && data.containsKey(coin.toLowerCase())) {
            CoinBalance balance = data.get(coin.toLowerCase());
            return balance != null ? balance.getFree() : BigDecimal.ZERO;
        }
        return BigDecimal.ZERO;
    }
}