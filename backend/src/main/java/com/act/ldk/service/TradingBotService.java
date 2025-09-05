package com.act.ldk.service;

import com.act.ldk.domain.entity.ForceTradeSettings;
import com.act.ldk.domain.entity.OrderBookSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.ForceTradeSettingsRepository;
import com.act.ldk.domain.repository.OrderBookSettingsRepository;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import com.act.ldk.dto.TradeSettingsDto;
import com.act.ldk.external.lbank.service.LBankApiService;
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
    private final LBankApiService lbankApiService;
    
    /**
     * 거래 설정 조회
     */
    public TradeSettings getTradeSettings(String symbol) {
        return tradeSettingsRepository.findBySymbol(symbol)
            .orElseGet(() -> createDefaultTradeSettings(symbol));
    }
    
    /**
     * 거래 설정 조회 (DTO)
     */
    public TradeSettingsDto getTradeSettingsDto(String symbol) {
        TradeSettings settings = getTradeSettings(symbol);
        BigDecimal calculatedTargetPrice = null;
        BigDecimal followCoinPrice = null;
        
        // 팔로우 코인이 활성화되어 있고 비율이 설정되어 있으면 목표 가격 계산
        if (settings.getFollowCoinEnabled() && settings.getFollowCoin() != null && settings.getFollowCoinRate() != null) {
            try {
                String followSymbol = settings.getFollowCoin().toLowerCase() + "_usdt";
                followCoinPrice = lbankApiService.getFollowCoinPrice(followSymbol);
                
                if (followCoinPrice != null) {
                    // 목표 LDK 가격 = 추종 코인 가격 * 목표 비율
                    calculatedTargetPrice = followCoinPrice.multiply(settings.getFollowCoinRate());
                    log.debug("추종 모드 목표가격 계산: {}={}, 비율={}, LDK목표가={}", 
                        settings.getFollowCoin(), followCoinPrice, settings.getFollowCoinRate(), calculatedTargetPrice);
                }
            } catch (Exception e) {
                log.error("추종 코인 가격 조회 실패", e);
            }
        } else {
            // 추종 모드가 아닐 때는 기본 목표가격 사용
            calculatedTargetPrice = settings.getTargetPrice();
        }
        
        return TradeSettingsDto.from(settings, calculatedTargetPrice, followCoinPrice);
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
        
        // 팔로우 코인 변경 체크
        boolean followCoinChanged = existing.getFollowCoin() != null && 
            settings.getFollowCoin() != null && 
            !existing.getFollowCoin().equals(settings.getFollowCoin());
        
        existing.setFollowCoinEnabled(settings.getFollowCoinEnabled());
        existing.setFollowCoin(settings.getFollowCoin());
        
        // 팔로우 코인이 비활성화되거나 변경되면 비율 관련 데이터 초기화
        if (!settings.getFollowCoinEnabled()) {
            existing.setFollowCoinRate(null);
            existing.setFollowCoinRateFormula(null);
            log.info("팔로우 코인 비활성화, 비율 데이터 초기화");
        } else if (followCoinChanged) {
            existing.setFollowCoinRate(null);
            existing.setFollowCoinRateFormula(null);
            log.info("팔로우 코인 변경 ({}), 비율 데이터 초기화", settings.getFollowCoin());
        }
        
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