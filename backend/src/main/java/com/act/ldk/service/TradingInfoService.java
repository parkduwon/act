package com.act.ldk.service;

import com.act.ldk.service.dto.TradingInfoResponse;
import com.act.ldk.external.lbank.module.CustomerAssetInfo;
import com.act.ldk.external.lbank.module.UserInfoAccount;
import com.act.ldk.external.lbank.response.ResUserInfoAccountVo;
import com.act.ldk.external.lbank.service.LBankApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TradingInfoService {
    
    private final LBankApiService lbankApiService;
    
    public TradingInfoResponse getTradingInfo(String symbol) {
        try {
            BigDecimal currentPrice = lbankApiService.getLatestPrice(symbol);
            
            ResUserInfoAccountVo accountInfo = lbankApiService.getUserInfoAccount();
            
            TradingInfoResponse.PriceInfo priceInfo = TradingInfoResponse.PriceInfo.builder()
                .currentPrice(currentPrice)
                .changePercent(BigDecimal.ZERO) 
                .volume24h(BigDecimal.ZERO)
                .build();
            
            TradingInfoResponse.BalanceInfo ldkBalance = extractBalance(accountInfo, "ldk");
            TradingInfoResponse.BalanceInfo usdtBalance = extractBalance(accountInfo, "usdt");
            
            return TradingInfoResponse.builder()
                .priceInfo(priceInfo)
                .ldkBalance(ldkBalance)
                .usdtBalance(usdtBalance)
                .build();
                
        } catch (Exception e) {
            log.error("Failed to get trading info for {}", symbol, e);
            return createEmptyResponse();
        }
    }
    
    private TradingInfoResponse.BalanceInfo extractBalance(ResUserInfoAccountVo accountInfo, String currency) {
        if (accountInfo == null || accountInfo.getData() == null || accountInfo.getData().getBalances() == null) {
            return createEmptyBalance(currency);
        }
        
        UserInfoAccount data = accountInfo.getData();
        
        for (CustomerAssetInfo assetInfo : data.getBalances()) {
            if (assetInfo.getAsset().equalsIgnoreCase(currency)) {
                BigDecimal free = parseBigDecimal(assetInfo.getFree());
                BigDecimal locked = parseBigDecimal(assetInfo.getLocked());
                BigDecimal total = free.add(locked);
                
                return TradingInfoResponse.BalanceInfo.builder()
                    .currency(currency.toUpperCase())
                    .free(free)
                    .locked(locked)
                    .total(total)
                    .build();
            }
        }
        
        return createEmptyBalance(currency);
    }
    
    private BigDecimal parseBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        try {
            if (value instanceof BigDecimal) {
                return (BigDecimal) value;
            }
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            log.warn("Failed to parse BigDecimal from value: {}", value);
            return BigDecimal.ZERO;
        }
    }
    
    private TradingInfoResponse.BalanceInfo createEmptyBalance(String currency) {
        return TradingInfoResponse.BalanceInfo.builder()
            .currency(currency.toUpperCase())
            .free(BigDecimal.ZERO)
            .locked(BigDecimal.ZERO)
            .total(BigDecimal.ZERO)
            .build();
    }
    
    private TradingInfoResponse createEmptyResponse() {
        return TradingInfoResponse.builder()
            .priceInfo(TradingInfoResponse.PriceInfo.builder()
                .currentPrice(BigDecimal.ZERO)
                .changePercent(BigDecimal.ZERO)
                .volume24h(BigDecimal.ZERO)
                .build())
            .ldkBalance(createEmptyBalance("ldk"))
            .usdtBalance(createEmptyBalance("usdt"))
            .build();
    }
}