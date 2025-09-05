package com.act.ldk.service;

import com.act.ldk.domain.entity.ForceTradeSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.ForceTradeSettingsRepository;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import com.act.ldk.external.lbank.module.CustomerAssetInfo;
import com.act.ldk.external.lbank.response.OrderBookResponse;
import com.act.ldk.external.lbank.response.PlaceOrderResponse;
import com.act.ldk.external.lbank.response.ResUserInfoAccountVo;
import com.act.ldk.external.lbank.response.TickerResponse;
import com.act.ldk.external.lbank.service.LBankApiService;
import com.act.ldk.service.dto.TradeContext;
import com.act.ldk.util.TradeUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TradeExecutionService {
    
    private final LBankApiService lbankApiService;
    private final TradeSettingsRepository tradeSettingsRepository;
    private final ForceTradeSettingsRepository forceTradeSettingsRepository;
    private final Random random = new Random();
    
    /**
     * 거래 실행 메인 메소드
     */
    public void executeTrade(String symbol) {
        try {
            // 1. 거래 설정 확인
            TradeSettings tradeSettings = tradeSettingsRepository.findBySymbol(symbol)
                .filter(TradeSettings::getEnabled)
                .orElse(null);
            if (tradeSettings == null) {
                log.debug("거래 설정이 없거나 비활성화됨: {}", symbol);
                return;
            }
            
            // 2. 강제 종료 확인
            ForceTradeSettings forceSettings = forceTradeSettingsRepository.findBySymbol(symbol)
                .orElse(null);
            if (forceSettings != null && forceSettings.getForceStopEnabled()) {
                log.info("강제 종료 활성화로 거래 중지: {}", symbol);
                return;
            }
            
            // 3. 랜덤 확률 체크
            if (!shouldExecuteTrade(tradeSettings.getRandomTryPercent())) {
                log.debug("랜덤 확률로 거래 스킵: {}", symbol);
                return;
            }
            
            // 4. 거래 컨텍스트 생성
            TradeContext context = buildTradeContext(tradeSettings, forceSettings);
            
            // 5. 거래 타입 결정
            determineTradeType(context, tradeSettings, forceSettings);
            
            // 6. 거래 실행
            performTrade(context);
            
        } catch (Exception e) {
            log.error("거래 실행 중 오류 발생: {}", symbol, e);
        }
    }
    
    /**
     * 랜덤 확률로 거래 실행 여부 결정
     */
    private boolean shouldExecuteTrade(Integer randomTryPercent) {
        if (randomTryPercent == null || randomTryPercent <= 0) {
            return false;
        }
        return random.nextInt(100) < randomTryPercent;
    }
    
    /**
     * 거래 컨텍스트 생성
     */
    private TradeContext buildTradeContext(TradeSettings settings, ForceTradeSettings forceSettings) {
        // 현재가 조회
        TickerResponse ticker = lbankApiService.getTicker(settings.getSymbol());
        BigDecimal currentPrice = ticker.getCurrentPrice();
        
        // 계좌 잔고 조회
        BigDecimal usdtBalance = BigDecimal.ZERO;
        BigDecimal mainCoinBalance = BigDecimal.ZERO;
        try {
            ResUserInfoAccountVo account = lbankApiService.getUserInfoAccount();
            usdtBalance = extractBalance(account, "usdt");
            mainCoinBalance = extractBalance(account, settings.getMainCoin().toLowerCase());
        } catch (Exception e) {
            log.error("계좌 정보 조회 실패", e);
        }
        
        return TradeContext.builder()
            .symbol(settings.getSymbol())
            .mainCoin(settings.getMainCoin())
            .quoteCoin(settings.getQuoteCoin())
            .currentPrice(currentPrice)
            .targetPrice(settings.getTargetPrice())
            .boundDollar(settings.getBoundDollar())
            .usdtBalance(usdtBalance)
            .mainCoinBalance(mainCoinBalance)
            .forceType(forceSettings != null ? forceSettings.getForceType() : ForceTradeSettings.ForceType.NONE)
            .forceTradeActive(forceSettings != null && !forceSettings.getForceType().equals(ForceTradeSettings.ForceType.NONE))
            .build();
    }
    
    /**
     * 계좌 잔고 조회
     */
    private BigDecimal extractBalance(ResUserInfoAccountVo account, String currency) {
        if (account == null || account.getData() == null || account.getData().getBalances() == null) {
            return BigDecimal.ZERO;
        }
        
        for (CustomerAssetInfo asset : account.getData().getBalances()) {
            if (asset.getAsset().equalsIgnoreCase(currency)) {
                try {
                    return asset.getFree() != null ? new BigDecimal(asset.getFree()) : BigDecimal.ZERO;
                } catch (NumberFormatException e) {
                    log.error("잔고 파싱 오류: {}", asset.getFree());
                    return BigDecimal.ZERO;
                }
            }
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * 거래 타입 결정
     */
    private void determineTradeType(TradeContext context, TradeSettings settings, ForceTradeSettings forceSettings) {
        // 강제 거래 모드 확인
        if (context.isForceTradeActive() && forceSettings != null) {
            handleForceTrade(context, settings, forceSettings);
            return;
        }
        
        // 일반 거래 모드
        if (settings.getFollowCoinEnabled() && settings.getFollowCoin() != null && !settings.getFollowCoin().isEmpty()) {
            // 추종 모드 - followCoinEnabled가 true이고 followCoin이 설정되어 있으면 추종 모드
            handleFollowTrade(context, settings);
        } else {
            // 목표가 모드
            handleTargetPriceTrade(context, settings);
        }
    }
    
    /**
     * 강제 거래 처리
     */
    private void handleForceTrade(TradeContext context, TradeSettings settings, ForceTradeSettings forceSettings) {
        switch (forceSettings.getForceType()) {
            case MAX_PRICE:
                if (context.getCurrentPrice().compareTo(settings.getMaxTradePrice()) >= 0) {
                    context.setOrderSide(ForceTradeSettings.OrderSide.SELL);
                    log.info("최고가 도달로 강제 매도: {}", context.getCurrentPrice());
                }
                break;
            case MIN_PRICE:
                if (context.getCurrentPrice().compareTo(settings.getMinTradePrice()) <= 0) {
                    context.setOrderSide(ForceTradeSettings.OrderSide.BUY);
                    log.info("최저가 도달로 강제 매수: {}", context.getCurrentPrice());
                }
                break;
            case MIN_USDT_ASSET:
                if (context.getUsdtBalance().compareTo(settings.getMinUsdtQuantity()) < 0) {
                    context.setOrderSide(ForceTradeSettings.OrderSide.SELL);
                    log.info("USDT 부족으로 강제 매도: {}", context.getUsdtBalance());
                }
                break;
            default:
                break;
        }
    }
    
    /**
     * 추종 모드 거래
     * 메인 코인과 추종 코인의 비율을 기준으로 거래 결정
     */
    private void handleFollowTrade(TradeContext context, TradeSettings settings) {
        // 추종 코인이 선택되지 않으면 목표가 모드로 처리
        if (settings.getFollowCoin() == null || settings.getFollowCoin().isEmpty()) {
            log.debug("추종 코인 미선택. 목표가 모드로 동작");
            handleTargetPriceTrade(context, settings);
            return;
        }
        
        try {
            // 추종 코인 심볼 생성 (예: ltc_usdt, trx_usdt - LBank는 USDT 마켓만 사용)
            String followSymbol = settings.getFollowCoin().toLowerCase() + "_usdt";
            
            // 추종 코인 가격 조회
            BigDecimal followPrice = lbankApiService.getFollowCoinPrice(followSymbol);
            if (followPrice == null) {
                log.error("추종 코인 {} 가격 조회 실패", followSymbol);
                handleTargetPriceTrade(context, settings);
                return;
            }
            
            // 현재 비율 계산 (메인코인 가격 / 추종코인 가격)
            BigDecimal currentRatio = TradeUtils.calculateFollowRatio(followPrice, context.getCurrentPrice());
            
            // 추종 비율이 없으면 현재 비율을 초기 비율로 저장
            if (settings.getFollowCoinRate() == null) {
                settings.setFollowCoinRate(currentRatio);
                settings.setFollowCoinRateFormula(String.format("LDK/%s = %.8f (초기 비율)", 
                    settings.getFollowCoin().toUpperCase(), currentRatio));
                tradeSettingsRepository.save(settings);
                log.info("추종 모드 시작, 초기 비율 저장: LDK/{}={}", settings.getFollowCoin(), currentRatio);
            }
            
            BigDecimal targetRatio = settings.getFollowCoinRate();
            
            // 목표 비율에 따른 LDK 목표 가격 계산
            // 목표 LDK 가격 = 추종 코인 가격 * 목표 비율
            BigDecimal targetLdkPrice = followPrice.multiply(targetRatio);
            
            log.info("추종 모드: {}={}, LDK현재가={}, LDK목표가={}, 현재비율={}, 목표비율={}", 
                settings.getFollowCoin(), followPrice, context.getCurrentPrice(), 
                targetLdkPrice, currentRatio, targetRatio);
            
            // 비율 기준으로 거래 결정
            // 현재 비율이 목표 비율보다 높으면 매도 (LDK가 상대적으로 비쌈)
            // 현재 비율이 목표 비율보다 낮으면 매수 (LDK가 상대적으로 저렴)
            if (currentRatio.compareTo(targetRatio) > 0) {
                context.setOrderSide(ForceTradeSettings.OrderSide.SELL);
                log.info("비율이 목표보다 높아 매도: 현재비율={}, 목표비율={}", currentRatio, targetRatio);
            } else {
                context.setOrderSide(ForceTradeSettings.OrderSide.BUY);
                log.info("비율이 목표보다 낮아 매수: 현재비율={}, 목표비율={}", currentRatio, targetRatio);
            }
            
        } catch (Exception e) {
            log.error("추종 모드 처리 중 오류 발생", e);
            handleTargetPriceTrade(context, settings);
        }
    }
    
    /**
     * 목표가 모드 거래
     */
    private void handleTargetPriceTrade(TradeContext context, TradeSettings settings) {
        if (context.getCurrentPrice().compareTo(context.getTargetPrice()) > 0) {
            context.setOrderSide(ForceTradeSettings.OrderSide.SELL);
            log.info("목표가보다 높아 매도: 현재가={}, 목표가={}", context.getCurrentPrice(), context.getTargetPrice());
        } else {
            context.setOrderSide(ForceTradeSettings.OrderSide.BUY);
            log.info("목표가보다 낮아 매수: 현재가={}, 목표가={}", context.getCurrentPrice(), context.getTargetPrice());
        }
    }
    
    /**
     * 실제 거래 실행
     */
    private void performTrade(TradeContext context) {
        if (context.getOrderSide() == null || context.getOrderSide() == ForceTradeSettings.OrderSide.NONE) {
            log.debug("거래 타입이 결정되지 않음");
            return;
        }
        
        // 호가창 조회
        OrderBookResponse orderBook = lbankApiService.getOrderBook(context.getSymbol(), 10, null);
        
        // 호가창 정보 로그
        log.info("호가창 상태 - 매수 최고가: {}, 매도 최저가: {}", 
            orderBook.getBestBidPrice(), orderBook.getBestAskPrice());
        
        // 가격 결정
        BigDecimal price = determinePrice(context, orderBook);
        
        // 수량 결정
        BigDecimal quantity = determineQuantity(context, price);
        
        // 잔고 확인
        if (!validateBalance(context, price, quantity)) {
            log.warn("잔고 부족으로 거래 취소");
            return;
        }
        
        // 주문 실행
        String type = context.getOrderSide() == ForceTradeSettings.OrderSide.BUY ? "buy" : "sell";
        PlaceOrderResponse response = lbankApiService.createOrder(
            context.getSymbol(),
            type,
            price.toPlainString(),
            quantity.toPlainString()
        );
        
        if (response != null && response.getOrderId() != null) {
            log.info("주문 성공: orderId={}, type={}, price={}, quantity={}", 
                response.getOrderId(), type, price, quantity);
        } else {
            log.error("주문 실패: errorCode={}", response != null ? response.getErrorCode() : "null response");
        }
    }
    
    /**
     * 거래 가격 결정
     */
    private BigDecimal determinePrice(TradeContext context, OrderBookResponse orderBook) {
        BigDecimal currentPrice = context.getCurrentPrice();
        BigDecimal targetPrice = context.getTargetPrice();
        BigDecimal priceStep = new BigDecimal("0.00001"); // 최소 단위
        BigDecimal price;
        
        if (context.getOrderSide() == ForceTradeSettings.OrderSide.BUY) {
            // 매수: 목표가가 현재가보다 높음 (가격 상승 기대)
            BigDecimal bestAskPrice = orderBook.getBestAskPrice();
            
            if (bestAskPrice != null && bestAskPrice.compareTo(BigDecimal.ZERO) > 0) {
                // 매도 호가가 있으면 체결
                price = bestAskPrice;
                log.info("매수 - 매도 호가 먹기: 가격={}", price);
            } else {
                // 매도 호가가 없으면 현재가와 목표가 사이에 호가 생성
                // 현재가보다 높되, 목표가보다는 낮은 가격
                int steps = 1 + random.nextInt(5); // 1~5 스텝
                BigDecimal offset = priceStep.multiply(new BigDecimal(steps));
                price = currentPrice.add(offset);
                
                // 목표가를 넘지 않도록
                if (price.compareTo(targetPrice) > 0) {
                    price = targetPrice.subtract(priceStep);
                }
                
                log.info("매수 호가 생성: 현재가={}, 목표가={}, 주문가={}", 
                    currentPrice, targetPrice, price);
            }
        } else {
            // 매도: 체결 우선
            BigDecimal bestBidPrice = orderBook.getBestBidPrice();
            BigDecimal bestAskPrice = orderBook.getBestAskPrice();
            
            // 1. 매수 호가가 있으면 그 가격에 매도 (즉시 체결)
            if (bestBidPrice != null && bestBidPrice.compareTo(BigDecimal.ZERO) > 0) {
                price = bestBidPrice;
                log.info("매도 - 매수 호가에 체결: 가격={}", price);
            } 
            // 2. 매수 호가는 없지만 매도 호가가 있으면, 매도 호가보다 낮은 가격으로
            else if (bestAskPrice != null && bestAskPrice.compareTo(BigDecimal.ZERO) > 0) {
                // 현재 매도 호가보다 1스텝 낮은 가격 (언더커팅)
                price = bestAskPrice.subtract(priceStep);
                // 현재가보다는 낮게
                if (price.compareTo(currentPrice) > 0) {
                    price = currentPrice.subtract(priceStep);
                }
                log.info("매도 - 매도 호가 언더커팅: 현재 매도호가={}, 주문가={}", bestAskPrice, price);
            }
            // 3. 아무 호가도 없으면 현재가 근처에 매도 호가 생성
            else {
                // 현재가보다 약간 낮은 가격
                int steps = 1 + random.nextInt(3); // 1~3 스텝
                price = currentPrice.subtract(priceStep.multiply(new BigDecimal(steps)));
                log.info("매도 - 호가 없음, 새 매도 호가: 현재가={}, 주문가={}", currentPrice, price);
            }
        }
        
        return price.setScale(5, RoundingMode.HALF_UP); // LDK 5자리 소숫점
    }
    
    /**
     * 거래 수량 결정
     * ActProject 로직과 동일: 최소수량(1) + 랜덤범위
     */
    private BigDecimal determineQuantity(TradeContext context, BigDecimal price) {
        // 최소 수량 = 1
        BigDecimal minQuantity = BigDecimal.ONE;
        
        // boundDollar를 현재가격으로 나눈 값이 랜덤 범위의 최대값
        double maxRandomRange = context.getBoundDollar().doubleValue() / price.doubleValue();
        
        // 0 ~ maxRandomRange 범위의 랜덤값 생성
        BigDecimal randomQuantity = new BigDecimal(random.nextDouble() * maxRandomRange);
        
        // 최종 수량 = 최소수량 + 랜덤수량
        return minQuantity.add(randomQuantity).setScale(2, RoundingMode.DOWN);
    }
    
    /**
     * 잔고 검증
     */
    private boolean validateBalance(TradeContext context, BigDecimal price, BigDecimal quantity) {
        if (context.getOrderSide() == ForceTradeSettings.OrderSide.BUY) {
            BigDecimal requiredUsdt = price.multiply(quantity);
            return context.getUsdtBalance().compareTo(requiredUsdt) >= 0;
        } else {
            return context.getMainCoinBalance().compareTo(quantity) >= 0;
        }
    }
}