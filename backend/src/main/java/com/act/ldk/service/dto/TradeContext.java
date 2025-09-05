package com.act.ldk.service.dto;

import com.act.ldk.domain.entity.ForceTradeSettings;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradeContext {
    private String symbol;
    private String mainCoin;
    private String quoteCoin;
    private BigDecimal currentPrice;
    private BigDecimal targetPrice;
    private BigDecimal boundDollar;
    private BigDecimal usdtBalance;
    private BigDecimal mainCoinBalance;
    private ForceTradeSettings.OrderSide orderSide;
    private ForceTradeSettings.ForceType forceType;
    private boolean forceTradeActive;
}