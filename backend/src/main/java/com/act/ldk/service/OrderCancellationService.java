package com.act.ldk.service;

import com.act.ldk.domain.entity.OrderBookSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.OrderBookSettingsRepository;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import com.act.ldk.external.lbank.response.OpenOrdersResponse;
import com.act.ldk.external.lbank.response.TickerResponse;
import com.act.ldk.external.lbank.service.LBankApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCancellationService {
    
    private final LBankApiService lbankApiService;
    private final TradeSettingsRepository tradeSettingsRepository;
    private final OrderBookSettingsRepository orderBookSettingsRepository;
    
    /**
     * 오래된 주문 취소
     */
    public void cancelOldOrders(String symbol) {
        try {
            // 설정 조회
            TradeSettings tradeSettings = tradeSettingsRepository.findBySymbol(symbol)
                .filter(TradeSettings::getEnabled)
                .orElse(null);
            
            if (tradeSettings == null) {
                log.debug("거래 설정이 없거나 비활성화됨: {}", symbol);
                return;
            }
            
            OrderBookSettings orderBookSettings = orderBookSettingsRepository.findBySymbol(symbol)
                .orElse(null);
            
            // 현재가 조회
            TickerResponse ticker = lbankApiService.getTicker(symbol);
            BigDecimal currentPrice = ticker.getCurrentPrice();
            
            // 열린 주문 조회
            OpenOrdersResponse openOrders = lbankApiService.getOpenOrders(symbol, "1", "200");
            List<OpenOrdersResponse.Order> orders = openOrders.getOrders();
            
            if (orders.isEmpty()) {
                log.debug("취소할 주문이 없습니다: {}", symbol);
                return;
            }
            
            // 최대/최소 가격 범위 설정
            BigDecimal maxPrice = currentPrice.multiply(new BigDecimal("1.5"));
            BigDecimal minPrice = currentPrice.multiply(new BigDecimal("0.5"));
            
            if (orderBookSettings != null) {
                maxPrice = currentPrice.add(orderBookSettings.getAskOrderBookEndPrice());
                minPrice = currentPrice.subtract(orderBookSettings.getBidOrderBookEndPrice());
            }
            
            // 가격 범위 벗어난 주문만 취소
            for (OpenOrdersResponse.Order order : orders) {
                // 가격 범위 확인 (최대가격 초과 또는 최소가격 미만인 경우만)
                if (order.getPrice().compareTo(maxPrice) > 0 || order.getPrice().compareTo(minPrice) < 0) {
                    try {
                        lbankApiService.cancelOrder(symbol, order.getOrderId());
                        log.info("가격 범위 벗어난 주문 취소: orderId={}, 가격={}, 범위=[{}-{}]", 
                            order.getOrderId(), order.getPrice(), minPrice, maxPrice);
                    } catch (Exception e) {
                        log.error("주문 취소 실패: orderId={}", order.getOrderId(), e);
                    }
                    
                    // API 제한 방지를 위한 지연
                    Thread.sleep(100);
                }
            }
            
        } catch (Exception e) {
            log.error("오래된 주문 취소 중 오류: {}", symbol, e);
        }
    }
}