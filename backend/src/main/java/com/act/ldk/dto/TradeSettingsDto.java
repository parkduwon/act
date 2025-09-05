package com.act.ldk.dto;

import com.act.ldk.domain.entity.TradeSettings;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TradeSettingsDto {
    private Long id;
    private String symbol;
    private String mainCoin;
    private String quoteCoin;
    private BigDecimal targetPrice;
    private BigDecimal maxTradePrice;
    private BigDecimal minTradePrice;
    private BigDecimal minUsdtQuantity;
    private BigDecimal boundDollar;
    private Integer randomTryPercent;
    private Boolean followCoinEnabled;
    private String followCoin;
    private BigDecimal followCoinRate;
    private String followCoinRateFormula;
    private BigDecimal followTargetPrice;  // 추종 모드 실시간 목표 가격
    private BigDecimal followCoinCurrentPrice;  // 추종 코인 현재 가격
    private Boolean bidTradeSwitch;
    private Integer bidTradeScheduleRate;
    private BigDecimal bidTradeDollar;
    private Boolean enabled;
    
    public static TradeSettingsDto from(TradeSettings settings, BigDecimal calculatedTargetPrice, BigDecimal followCoinPrice) {
        TradeSettingsDto dto = new TradeSettingsDto();
        dto.setId(settings.getId());
        dto.setSymbol(settings.getSymbol());
        dto.setMainCoin(settings.getMainCoin());
        dto.setQuoteCoin(settings.getQuoteCoin());
        dto.setTargetPrice(settings.getTargetPrice());
        dto.setMaxTradePrice(settings.getMaxTradePrice());
        dto.setMinTradePrice(settings.getMinTradePrice());
        dto.setMinUsdtQuantity(settings.getMinUsdtQuantity());
        dto.setBoundDollar(settings.getBoundDollar());
        dto.setRandomTryPercent(settings.getRandomTryPercent());
        dto.setFollowCoinEnabled(settings.getFollowCoinEnabled());
        dto.setFollowCoin(settings.getFollowCoin());
        dto.setFollowCoinRate(settings.getFollowCoinRate());
        dto.setFollowCoinRateFormula(settings.getFollowCoinRateFormula());
        dto.setFollowTargetPrice(calculatedTargetPrice);
        dto.setFollowCoinCurrentPrice(followCoinPrice);
        dto.setBidTradeSwitch(settings.getBidTradeSwitch());
        dto.setBidTradeScheduleRate(settings.getBidTradeScheduleRate());
        dto.setBidTradeDollar(settings.getBidTradeDollar());
        dto.setEnabled(settings.getEnabled());
        return dto;
    }
}