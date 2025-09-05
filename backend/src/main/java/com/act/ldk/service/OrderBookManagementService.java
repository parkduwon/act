package com.act.ldk.service;

import com.act.ldk.domain.entity.ForceTradeSettings;
import com.act.ldk.domain.entity.OrderBookSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.ForceTradeSettingsRepository;
import com.act.ldk.domain.repository.OrderBookSettingsRepository;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import com.act.ldk.external.lbank.module.CustomerAssetInfo;
import com.act.ldk.external.lbank.response.OpenOrdersResponse;
import com.act.ldk.external.lbank.response.PlaceOrderResponse;
import com.act.ldk.external.lbank.response.ResUserInfoAccountVo;
import com.act.ldk.external.lbank.response.TickerResponse;
import com.act.ldk.external.lbank.service.LBankApiService;
import com.act.ldk.service.dto.OrderBookContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderBookManagementService {
    
    private final LBankApiService lbankApiService;
    private final OrderBookSettingsRepository orderBookSettingsRepository;
    private final TradeSettingsRepository tradeSettingsRepository;
    private final ForceTradeSettingsRepository forceTradeSettingsRepository;
    private final Random random = new Random();
    
    /**
     * 호가창 관리 메인 메소드
     */
    public void manageOrderBook(String symbol) {
        try {
            // 1. 설정 확인
            OrderBookSettings orderBookSettings = orderBookSettingsRepository.findBySymbol(symbol)
                .filter(OrderBookSettings::getEnabled)
                .orElse(null);
            if (orderBookSettings == null) {
                log.debug("호가창 설정이 없거나 비활성화됨: {}", symbol);
                return;
            }
            
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
                log.info("강제 종료 활성화로 호가창 관리 중지: {}", symbol);
                return;
            }
            
            // 3. 호가창 OFF 상태 확인
            if (orderBookSettings.getAskStopYN() && orderBookSettings.getBidStopYN()) {
                log.info("호가창 OFF 상태로 중지: {}", symbol);
                return;
            }
            
            // 4. 호가창 생성 조건 확인
            if (!checkOrderBookConditions(symbol, tradeSettings)) {
                return;
            }
            
            // 5. 호가창 컨텍스트 생성
            OrderBookContext context = buildOrderBookContext(orderBookSettings, tradeSettings);
            
            // 6. 호가창 관리 실행
            manageOrderBookOrders(context);
            
        } catch (Exception e) {
            log.error("호가창 관리 중 오류 발생: {}", symbol, e);
        }
    }
    
    /**
     * 호가창 생성 조건 확인
     */
    private boolean checkOrderBookConditions(String symbol, TradeSettings settings) {
        // 현재가 조회
        TickerResponse ticker = lbankApiService.getTicker(symbol);
        BigDecimal currentPrice = ticker.getCurrentPrice();
        
        // USDT 잔고 조회
        BigDecimal usdtBalance;
        try {
            ResUserInfoAccountVo account = lbankApiService.getUserInfoAccount();
            usdtBalance = extractBalance(account, "usdt");
            
            // 디버깅을 위한 상세 로그
            log.info("USDT 잔고 조회 결과: {}", usdtBalance);
            log.info("설정된 최소 USDT 수량: {}", settings.getMinUsdtQuantity());
            log.info("잔고 비교 결과: {} < {} = {}", 
                usdtBalance, settings.getMinUsdtQuantity(), 
                usdtBalance.compareTo(settings.getMinUsdtQuantity()) < 0);
        } catch (Exception e) {
            log.error("계좌 정보 조회 실패", e);
            return false;
        }
        
        // 최고가/최저가 범위 확인
        if (currentPrice.compareTo(settings.getMaxTradePrice()) > 0) {
            log.info("현재가가 최고가 설정값 초과: 현재가={}, 최고가={}", currentPrice, settings.getMaxTradePrice());
            return false;
        }
        
        if (currentPrice.compareTo(settings.getMinTradePrice()) < 0) {
            log.info("현재가가 최저가 설정값 미만: 현재가={}, 최저가={}", currentPrice, settings.getMinTradePrice());
            return false;
        }
        
        // USDT 잔고 확인
        if (usdtBalance.compareTo(settings.getMinUsdtQuantity()) < 0) {
            log.info("USDT 잔고 부족: 잔고={}, 최소값={}", usdtBalance, settings.getMinUsdtQuantity());
            return false;
        }
        
        return true;
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
     * 호가창 컨텍스트 생성
     */
    private OrderBookContext buildOrderBookContext(OrderBookSettings orderBookSettings, TradeSettings tradeSettings) {
        // 현재가 조회
        TickerResponse ticker = lbankApiService.getTicker(tradeSettings.getSymbol());
        BigDecimal currentPrice = ticker.getCurrentPrice();
        
        return OrderBookContext.builder()
            .symbol(tradeSettings.getSymbol())
            .mainCoin(tradeSettings.getMainCoin())
            .currentPrice(currentPrice)
            .boundDollar(tradeSettings.getBoundDollar())
            .askOrderBookStartPrice(orderBookSettings.getAskOrderBookStartPrice())
            .askOrderBookEndPrice(orderBookSettings.getAskOrderBookEndPrice())
            .askOrderBookLimitCount(orderBookSettings.getAskOrderBookLimitCount())
            .askStopYN(orderBookSettings.getAskStopYN())
            .bidOrderBookStartPrice(orderBookSettings.getBidOrderBookStartPrice())
            .bidOrderBookEndPrice(orderBookSettings.getBidOrderBookEndPrice())
            .bidOrderBookLimitCount(orderBookSettings.getBidOrderBookLimitCount())
            .bidStopYN(orderBookSettings.getBidStopYN())
            .build();
    }
    
    /**
     * 호가창 주문 관리
     */
    private void manageOrderBookOrders(OrderBookContext context) {
        // 현재 열려있는 모든 주문 조회 (페이지네이션 포함)
        OpenOrdersResponse openOrders = lbankApiService.getAllOpenOrders(context.getSymbol());
        
        if (openOrders == null || openOrders.getData() == null || openOrders.getOrders() == null) {
            log.error("주문 조회 실패: openOrders={}", openOrders);
            return;
        }
        
        List<OpenOrdersResponse.Order> currentOrders = openOrders.getOrders();
        log.info("현재 열린 주문 총 {} 개", currentOrders.size());
        
        // 전체 주문이 너무 많으면 추가 생성 중단
        if (currentOrders.size() >= context.getAskOrderBookLimitCount() + context.getBidOrderBookLimitCount()) {
            log.warn("전체 주문이 이미 최대치 도달: 현재={}, 최대={}", 
                currentOrders.size(), 
                context.getAskOrderBookLimitCount() + context.getBidOrderBookLimitCount());
            return;
        }
        
        // 매도 호가 관리
        if (!context.getAskStopYN()) {
            manageSellOrders(context, currentOrders);
        }
        
        // 매수 호가 관리
        if (!context.getBidStopYN()) {
            manageBuyOrders(context, currentOrders);
        }
    }
    
    /**
     * 매도 호가 관리
     */
    private void manageSellOrders(OrderBookContext context, List<OpenOrdersResponse.Order> currentOrders) {
        log.info("=========================매도 호가 관리=========================");
        
        // 현재 매도 주문 필터링
        List<BigDecimal> askPrices = currentOrders.stream()
            .filter(order -> "sell".equals(order.getType()))
            .map(OpenOrdersResponse.Order::getPrice)
            .sorted()
            .toList();
        
        // 전체 매도 주문 개수 확인 (범위와 무관하게)
        int totalAskOrders = askPrices.size();
        
        // 유효 범위 계산 (디버깅용)
        BigDecimal maxAskPrice = context.getCurrentPrice().add(context.getAskOrderBookEndPrice());
        long validAskCount = askPrices.stream()
            .filter(price -> price.compareTo(maxAskPrice) <= 0)
            .count();
        
        log.info("매도 호가 현황: 전체 매도 주문={}, 유효 범위 내={}, 최대 개수={}, 현재가={}, 최대가격={}", 
            totalAskOrders, validAskCount, context.getAskOrderBookLimitCount(), 
            context.getCurrentPrice(), maxAskPrice);
        
        // 전체 주문이 이미 최대치 이상이면 추가하지 않음
        int ordersToPlace = Math.max(0, context.getAskOrderBookLimitCount() - totalAskOrders);
        
        if (ordersToPlace > 0) {
            log.info("매도 주문 필요: {} 개 (현재: {}, 최대: {})", 
                ordersToPlace, validAskCount, context.getAskOrderBookLimitCount());
            
            for (int i = 0; i < ordersToPlace; i++) {
                BigDecimal orderPrice = generateAskPrice(context);
                
                // 중복 가격 체크 (기존 주문과 중복되지 않는 경우만)
                if (!askPrices.contains(orderPrice)) {
                    BigDecimal quantity = generateRandomQuantity(orderPrice, context.getBoundDollar());
                    
                    log.info("매도 호가 추가: 가격={}, 수량={}", orderPrice, quantity);
                    
                    PlaceOrderResponse response = lbankApiService.createOrder(
                        context.getSymbol(),
                        "sell",
                        orderPrice.toPlainString(),
                        quantity.toPlainString()
                    );
                    
                    if (response != null && response.getOrderId() != null) {
                        log.info("매도 호가 주문 성공: orderId={}", response.getOrderId());
                    } else {
                        log.error("매도 호가 주문 실패: {}", response != null ? response.getErrorCode() : "null response");
                    }
                    
                    // API 제한 방지를 위한 지연
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    
    /**
     * 매수 호가 관리
     */
    private void manageBuyOrders(OrderBookContext context, List<OpenOrdersResponse.Order> currentOrders) {
        log.info("=========================매수 호가 관리=========================");
        
        // 현재 매수 주문 필터링
        List<BigDecimal> bidPrices = currentOrders.stream()
            .filter(order -> "buy".equals(order.getType()))
            .map(OpenOrdersResponse.Order::getPrice)
            .sorted(Comparator.reverseOrder()) // 내림차순 정렬
            .toList();
        
        // 전체 매수 주문 개수 확인 (범위와 무관하게)
        int totalBidOrders = bidPrices.size();
        
        // 유효 범위 계산 (디버깅용)
        BigDecimal minBidPrice = context.getCurrentPrice().subtract(context.getBidOrderBookEndPrice());
        long validBidCount = bidPrices.stream()
            .filter(price -> price.compareTo(minBidPrice) >= 0)
            .count();
        
        log.info("매수 호가 현황: 전체 매수 주문={}, 유효 범위 내={}, 최대 개수={}, 현재가={}, 최소가격={}", 
            totalBidOrders, validBidCount, context.getBidOrderBookLimitCount(), 
            context.getCurrentPrice(), minBidPrice);
        
        // 전체 주문이 이미 최대치 이상이면 추가하지 않음
        int ordersToPlace = Math.max(0, context.getBidOrderBookLimitCount() - totalBidOrders);
        
        if (ordersToPlace > 0) {
            log.info("매수 주문 필요: {} 개 (현재: {}, 최대: {})", 
                ordersToPlace, validBidCount, context.getBidOrderBookLimitCount());
            
            for (int i = 0; i < ordersToPlace; i++) {
                BigDecimal orderPrice = generateBidPrice(context);
                
                // 중복 가격 체크 및 양수 확인 (기존 주문과 중복되지 않는 경우만)
                if (!bidPrices.contains(orderPrice) && orderPrice.compareTo(BigDecimal.ZERO) > 0) {
                    
                    BigDecimal quantity = generateRandomQuantity(orderPrice, context.getBoundDollar());
                    
                    log.info("매수 호가 추가: 가격={}, 수량={}", orderPrice, quantity);
                    
                    PlaceOrderResponse response = lbankApiService.createOrder(
                        context.getSymbol(),
                        "buy",
                        orderPrice.toPlainString(),
                        quantity.toPlainString()
                    );
                    
                    if (response != null && response.getOrderId() != null) {
                        log.info("매수 호가 주문 성공: orderId={}", response.getOrderId());
                    } else {
                        log.error("매수 호가 주문 실패: {}", response != null ? response.getErrorCode() : "null response");
                    }
                    
                    // API 제한 방지를 위한 지연
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
    
    /**
     * 매도 가격 생성
     */
    private BigDecimal generateAskPrice(OrderBookContext context) {
        double range = context.getAskOrderBookEndPrice().subtract(context.getAskOrderBookStartPrice()).doubleValue();
        double randomOffset = context.getAskOrderBookStartPrice().doubleValue() + (random.nextDouble() * range);
        BigDecimal price = context.getCurrentPrice().add(new BigDecimal(randomOffset));
        
        // 마지막 자리 랜덤 처리 (0~9)
        double lastDigitRandom = random.nextInt(10) * 0.00001;
        price = price.setScale(4, RoundingMode.DOWN).add(new BigDecimal(lastDigitRandom));
        
        return price.setScale(5, RoundingMode.HALF_UP); // LDK 5자리 소숫점
    }
    
    /**
     * 매수 가격 생성
     */
    private BigDecimal generateBidPrice(OrderBookContext context) {
        double range = context.getBidOrderBookEndPrice().subtract(context.getBidOrderBookStartPrice()).doubleValue();
        double randomOffset = context.getBidOrderBookStartPrice().doubleValue() + (random.nextDouble() * range);
        BigDecimal price = context.getCurrentPrice().subtract(new BigDecimal(randomOffset));
        
        // 마지막 자리 랜덤 처리 (0~9)
        double lastDigitRandom = random.nextInt(10) * 0.00001;
        price = price.setScale(4, RoundingMode.DOWN).add(new BigDecimal(lastDigitRandom));
        
        return price.setScale(5, RoundingMode.HALF_UP); // LDK 5자리 소숫점
    }
    
    /**
     * 랜덤 수량 생성
     */
    private BigDecimal generateRandomQuantity(BigDecimal price, BigDecimal boundDollar) {
        double randomRatio = 0.1 + (0.9 * random.nextDouble()); // 10% ~ 100%
        BigDecimal dollarAmount = boundDollar.multiply(new BigDecimal(randomRatio));
        return dollarAmount.divide(price, 2, RoundingMode.DOWN);
    }
}