package com.act.ldk.service;

import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.domain.repository.TradeSettingsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * 거래 봇 스케줄러
 * Spring Batch 대신 단순 스케줄러로 구현
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TradingBotScheduler {
    
    private final TradeExecutionService tradeExecutionService;
    private final OrderBookManagementService orderBookManagementService;
    private final OrderCancellationService orderCancellationService;
    private final TradeSettingsRepository tradeSettingsRepository;
    private final Random random = new Random();
    
    /**
     * 거래 실행 스케줄러
     * 초기 지연: 3초, 실행 간격: 5초
     */
//    @Scheduled(initialDelay = 3000, fixedRate = 5000)
//    public void executeTrade() {
//        List<TradeSettings> activeSettings = tradeSettingsRepository.findByEnabled(true);
//
//        if (activeSettings.isEmpty()) {
//            log.debug("활성화된 거래 설정이 없습니다.");
//            return;
//        }
//
//        for (TradeSettings settings : activeSettings) {
//            try {
//                // 랜덤 지연 (0~3500ms) - 봇 탐지 회피
//                int randomDelay = random.nextInt(3500);
//                Thread.sleep(randomDelay);
//
//                log.debug("거래 실행 시작: {}", settings.getSymbol());
//                tradeExecutionService.executeTrade(settings.getSymbol());
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.error("거래 스케줄러 인터럽트 발생", e);
//                break;
//            } catch (Exception e) {
//                log.error("거래 실행 중 오류 발생: {}", settings.getSymbol(), e);
//            }
//        }
//    }
    
    /**
     * 호가창 관리 스케줄러
     * 초기 지연: 1초, 실행 간격: 60초
     */
//    @Scheduled(initialDelay = 1000, fixedRate = 60000)
//    public void manageOrderBook() {
//        List<TradeSettings> activeSettings = tradeSettingsRepository.findByEnabled(true);
//
//        if (activeSettings.isEmpty()) {
//            log.debug("활성화된 거래 설정이 없습니다.");
//            return;
//        }
//
//        for (TradeSettings settings : activeSettings) {
//            try {
//                // 랜덤 지연 (0~2500ms) - 봇 탐지 회피
//                int randomDelay = random.nextInt(2500);
//                Thread.sleep(randomDelay);
//
//                log.debug("호가창 관리 시작: {}", settings.getSymbol());
//                orderBookManagementService.manageOrderBook(settings.getSymbol());
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.error("호가창 관리 스케줄러 인터럽트 발생", e);
//                break;
//            } catch (Exception e) {
//                log.error("호가창 관리 중 오류 발생: {}", settings.getSymbol(), e);
//            }
//        }
//    }
    
    /**
     * 호가 취소 스케줄러
     * 초기 지연: 1초, 실행 간격: 10초
     */
//    @Scheduled(initialDelay = 1000, fixedRate = 10000)
//    public void cancelOldOrders() {
//        List<TradeSettings> activeSettings = tradeSettingsRepository.findByEnabled(true);
//
//        if (activeSettings.isEmpty()) {
//            log.debug("활성화된 거래 설정이 없습니다.");
//            return;
//        }
//
//        for (TradeSettings settings : activeSettings) {
//            try {
//                // 랜덤 지연 (0~2500ms) - 봇 탐지 회피
//                int randomDelay = random.nextInt(2500);
//                Thread.sleep(randomDelay);
//
//                log.debug("오래된 주문 취소 시작: {}", settings.getSymbol());
//                orderCancellationService.cancelOldOrders(settings.getSymbol());
//
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//                log.error("주문 취소 스케줄러 인터럽트 발생", e);
//                break;
//            } catch (Exception e) {
//                log.error("주문 취소 중 오류 발생: {}", settings.getSymbol(), e);
//            }
//        }
//    }
    
    /**
     * 봇 상태 로깅 (선택적)
     * 매 30분마다 현재 상태 로깅
     */
    @Scheduled(initialDelay = 60000, fixedRate = 1800000)
    public void logBotStatus() {
        List<TradeSettings> activeSettings = tradeSettingsRepository.findByEnabled(true);
        
        if (activeSettings.isEmpty()) {
            log.info("활성화된 거래 설정이 없습니다.");
            return;
        }
        
        log.info("거래 봇 상태 - 활성화된 심볼 수: {}", activeSettings.size());
        for (TradeSettings settings : activeSettings) {
            log.info("  - {}: 타겟가={}, 최대거래가={}, 최소거래가={}", 
                settings.getSymbol(), 
                settings.getTargetPrice(),
                settings.getMaxTradePrice(),
                settings.getMinTradePrice());
        }
    }
}