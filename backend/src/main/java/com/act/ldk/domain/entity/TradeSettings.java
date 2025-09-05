package com.act.ldk.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "trade_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeSettings extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String symbol;  // e.g., "ldk_usdt"
    
    @Column(nullable = false)
    private String mainCoin;  // e.g., "LDK"
    
    @Column(nullable = false)
    private String quoteCoin;  // e.g., "USDT"
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal targetPrice;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal maxTradePrice;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal minTradePrice;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal minUsdtQuantity;
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal boundDollar;  // 거래 단위 금액
    
    @Column(nullable = false)
    private Integer randomTryPercent;  // 랜덤 거래 확률 (0-100)
    
    @Column(nullable = false)
    private Boolean followCoinEnabled;  // 추종 모드 여부
    
    private String followCoin;  // 추종할 코인 (LTC, TRX)
    
    @Column(precision = 20, scale = 8)
    private BigDecimal followCoinRate;  // 추종 비율 (자동 계산됨)
    
    @Column(precision = 20, scale = 8)
    private BigDecimal followCoinRatePrice;  // 추종 가격
    
    private String followCoinRateFormula;  // 추종 가격 공식
    
    @Column(nullable = false)
    private Boolean bidTradeSwitch;  // 매도 거래 스위치
    
    @Column(nullable = false)
    private Integer bidTradeScheduleRate;  // 거래 체결 간격(초)
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal bidTradeDollar;  // 거래 주문량(USDT)
    
    @Column(nullable = false)
    private Boolean enabled;  // 봇 활성화 여부
    
    @PrePersist
    protected void onCreate() {
        enabled = true;
        bidTradeSwitch = true;
        bidTradeScheduleRate = 5;
        bidTradeDollar = new BigDecimal("5");
    }
}