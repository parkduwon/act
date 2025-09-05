package com.act.ldk.service;

import com.act.ldk.domain.entity.ForceTradeSettings;
import com.act.ldk.domain.entity.OrderBookSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.ForceTradeSettingsRepository;
import com.act.ldk.domain.repository.OrderBookSettingsRepository;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * 거래 봇 관리 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TradingBotService {
    
    private final TradeSettingsRepository tradeSettingsRepository;
    private final OrderBookSettingsRepository orderBookSettingsRepository;
    private final ForceTradeSettingsRepository forceTradeSettingsRepository;
    
    /**
     * 거래 설정 조회
     */
    public TradeSettings getTradeSettings(String symbol) {
        return tradeSettingsRepository.findBySymbol(symbol)
            .orElseGet(() -> createDefaultTradeSettings(symbol));
    }
    
    /**
     * 거래 설정 업데이트
     */
    public TradeSettings updateTradeSettings(TradeSettings settings) {
        TradeSettings existing = tradeSettingsRepository.findBySymbol(settings.getSymbol())
            .orElse(settings);
        
        existing.setTargetPrice(settings.getTargetPrice());
        existing.setMaxTradePrice(settings.getMaxTradePrice());
        existing.setMinTradePrice(settings.getMinTradePrice());
        existing.setMinUsdtQuantity(settings.getMinUsdtQuantity());
        existing.setBoundDollar(settings.getBoundDollar());
        existing.setRandomTryPercent(settings.getRandomTryPercent());
        existing.setFollowCoinEnabled(settings.getFollowCoinEnabled());
        existing.setFollowCoin(settings.getFollowCoin());
        // followCoinRate는 자동 계산되므로 여기서 설정하지 않음
        
        // 매도 거래 설정 업데이트
        existing.setBidTradeSwitch(settings.getBidTradeSwitch());
        existing.setBidTradeScheduleRate(settings.getBidTradeScheduleRate());
        existing.setBidTradeDollar(settings.getBidTradeDollar());
        
        return tradeSettingsRepository.save(existing);
    }
    
    /**
     * 호가창 설정 조회
     */
    public OrderBookSettings getOrderBookSettings(String symbol) {
        return orderBookSettingsRepository.findBySymbol(symbol)
            .orElseGet(() -> createDefaultOrderBookSettings(symbol));
    }
    
    /**
     * 호가창 설정 업데이트
     */
    public OrderBookSettings updateOrderBookSettings(OrderBookSettings settings) {
        OrderBookSettings existing = orderBookSettingsRepository.findBySymbol(settings.getSymbol())
            .orElse(settings);
        
        existing.setAskOrderBookStartPrice(settings.getAskOrderBookStartPrice());
        existing.setAskOrderBookEndPrice(settings.getAskOrderBookEndPrice());
        existing.setAskOrderBookLimitCount(settings.getAskOrderBookLimitCount());
        existing.setAskStopYN(settings.getAskStopYN());
        existing.setBidOrderBookStartPrice(settings.getBidOrderBookStartPrice());
        existing.setBidOrderBookEndPrice(settings.getBidOrderBookEndPrice());
        existing.setBidOrderBookLimitCount(settings.getBidOrderBookLimitCount());
        existing.setBidStopYN(settings.getBidStopYN());
        
        return orderBookSettingsRepository.save(existing);
    }
    
    /**
     * 강제 거래 설정 조회
     */
    public ForceTradeSettings getForceTradeSettings(String symbol) {
        return forceTradeSettingsRepository.findBySymbol(symbol)
            .orElseGet(() -> createDefaultForceTradeSettings(symbol));
    }
    
    /**
     * 강제 거래 설정 업데이트
     */
    public ForceTradeSettings updateForceTradeSettings(ForceTradeSettings settings) {
        ForceTradeSettings existing = forceTradeSettingsRepository.findBySymbol(settings.getSymbol())
            .orElse(settings);
        
        existing.setForceStopEnabled(settings.getForceStopEnabled());
        existing.setForceType(settings.getForceType());
        existing.setForceTradeType(settings.getForceTradeType());
        
        return forceTradeSettingsRepository.save(existing);
    }
    
    /**
     * 봇 활성화 여부 확인
     */
    public boolean isBotEnabled(String symbol) {
        return tradeSettingsRepository.findBySymbol(symbol)
            .map(TradeSettings::getEnabled)
            .orElse(false);
    }
    
    /**
     * 봇 시작
     */
    public void startBot(String symbol) {
        TradeSettings tradeSettings = getTradeSettings(symbol);
        tradeSettings.setEnabled(true);
        tradeSettingsRepository.save(tradeSettings);
        
        OrderBookSettings orderBookSettings = getOrderBookSettings(symbol);
        orderBookSettings.setEnabled(true);
        orderBookSettingsRepository.save(orderBookSettings);
        
        log.info("거래 봇 시작: {}", symbol);
    }
    
    /**
     * 봇 중지
     */
    public void stopBot(String symbol) {
        TradeSettings tradeSettings = getTradeSettings(symbol);
        tradeSettings.setEnabled(false);
        tradeSettingsRepository.save(tradeSettings);
        
        OrderBookSettings orderBookSettings = getOrderBookSettings(symbol);
        orderBookSettings.setEnabled(false);
        orderBookSettingsRepository.save(orderBookSettings);
        
        log.info("거래 봇 중지: {}", symbol);
    }
    
    /**
     * 강제 종료
     */
    public void forceStop(String symbol) {
        ForceTradeSettings forceSettings = getForceTradeSettings(symbol);
        forceSettings.setForceStopEnabled(true);
        forceTradeSettingsRepository.save(forceSettings);
        
        stopBot(symbol);
        
        log.info("거래 봇 강제 종료: {}", symbol);
    }
    
    /**
     * 기본 거래 설정 생성
     */
    private TradeSettings createDefaultTradeSettings(String symbol) {
        return TradeSettings.builder()
            .symbol(symbol)
            .mainCoin("LDK")
            .quoteCoin("USDT")
            .targetPrice(new BigDecimal("0.014500"))
            .maxTradePrice(new BigDecimal("0.02000"))
            .minTradePrice(new BigDecimal("0.01000"))
            .minUsdtQuantity(new BigDecimal("500"))
            .boundDollar(new BigDecimal("5"))
            .randomTryPercent(50)
            .followCoinEnabled(false)
            .bidTradeSwitch(true)
            .bidTradeScheduleRate(5)
            .bidTradeDollar(new BigDecimal("5"))
            .enabled(false)
            .build();
    }
    
    /**
     * 기본 호가창 설정 생성
     */
    private OrderBookSettings createDefaultOrderBookSettings(String symbol) {
        return OrderBookSettings.builder()
            .symbol(symbol)
            .askOrderBookStartPrice(new BigDecimal("0.001"))
            .askOrderBookEndPrice(new BigDecimal("0.01"))
            .askOrderBookLimitCount(10)
            .askStopYN(false)
            .bidOrderBookStartPrice(new BigDecimal("0.001"))
            .bidOrderBookEndPrice(new BigDecimal("0.01"))
            .bidOrderBookLimitCount(10)
            .bidStopYN(false)
            .enabled(false)
            .build();
    }
    
    /**
     * 기본 강제 거래 설정 생성
     */
    private ForceTradeSettings createDefaultForceTradeSettings(String symbol) {
        return ForceTradeSettings.builder()
            .symbol(symbol)
            .forceStopEnabled(false)
            .forceType(ForceTradeSettings.ForceType.NONE)
            .forceTradeType(ForceTradeSettings.OrderSide.NONE)
            .build();
    }
}