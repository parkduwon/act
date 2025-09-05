package com.act.ldk.external.lbank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TickerResponse {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("ts")
    private Long timestamp;
    
    @JsonProperty("data")
    private TickerData data;
    
    @Data
    public static class TickerData {
        @JsonProperty("symbol")
        private String symbol;
        
        @JsonProperty("latest")
        private BigDecimal latest;
        
        @JsonProperty("high")
        private BigDecimal high;
        
        @JsonProperty("low")
        private BigDecimal low;
        
        @JsonProperty("change")
        private BigDecimal change;
        
        @JsonProperty("vol")
        private BigDecimal vol;
        
        @JsonProperty("turnover")
        private BigDecimal turnover;
    }
    
    public BigDecimal getCurrentPrice() {
        return data != null ? data.getLatest() : BigDecimal.ZERO;
    }
}