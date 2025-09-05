package com.act.ldk.external.lbank.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderBookResponse {
    @JsonProperty("result")
    private String result;
    
    @JsonProperty("error_code")
    private Integer errorCode;
    
    @JsonProperty("ts")
    private Long timestamp;
    
    @JsonProperty("data")
    private OrderBookData data;
    
    @Data
    public static class OrderBookData {
        @JsonProperty("asks")
        private List<List<BigDecimal>> asks;
        
        @JsonProperty("bids")
        private List<List<BigDecimal>> bids;
    }
    
    public BigDecimal getBestAskPrice() {
        if (data != null && data.getAsks() != null && !data.getAsks().isEmpty()) {
            return data.getAsks().getFirst().getFirst();
        }
        return null;
    }
    
    public BigDecimal getBestBidPrice() {
        if (data != null && data.getBids() != null && !data.getBids().isEmpty()) {
            return data.getBids().getFirst().getFirst();
        }
        return null;
    }
}