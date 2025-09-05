package com.act.ldk.service.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderBookContext {
    private String symbol;
    private String mainCoin;
    private BigDecimal currentPrice;
    private BigDecimal boundDollar;
    
    // 매도 호가 설정
    private BigDecimal askOrderBookStartPrice;
    private BigDecimal askOrderBookEndPrice;
    private Integer askOrderBookLimitCount;
    private Boolean askStopYN;
    
    // 매수 호가 설정
    private BigDecimal bidOrderBookStartPrice;
    private BigDecimal bidOrderBookEndPrice;
    private Integer bidOrderBookLimitCount;
    private Boolean bidStopYN;
}