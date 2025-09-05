package com.act.ldk.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingInfoResponse {
    private PriceInfo priceInfo;
    private BalanceInfo ldkBalance;
    private BalanceInfo usdtBalance;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PriceInfo {
        private BigDecimal currentPrice;
        private BigDecimal changePercent;
        private BigDecimal volume24h;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BalanceInfo {
        private String currency;
        private BigDecimal free;
        private BigDecimal locked;
        private BigDecimal total;
    }
}