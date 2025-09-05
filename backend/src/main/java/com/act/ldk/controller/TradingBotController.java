package com.act.ldk.controller;

import com.act.ldk.domain.entity.ForceTradeSettings;
import com.act.ldk.domain.entity.OrderBookSettings;
import com.act.ldk.domain.entity.TradeSettings;
import com.act.ldk.service.TradingBotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 거래 봇 관리 API
 */
@Slf4j
@RestController
@RequestMapping("/api/bot")
@RequiredArgsConstructor
public class TradingBotController {
    
    private final TradingBotService tradingBotService;
    
    /**
     * 거래 봇 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getBotStatus(@RequestParam String symbol) {
        Map<String, Object> status = new HashMap<>();
        status.put("symbol", symbol);
        status.put("tradeSettings", tradingBotService.getTradeSettings(symbol));
        status.put("orderBookSettings", tradingBotService.getOrderBookSettings(symbol));
        status.put("forceTradeSettings", tradingBotService.getForceTradeSettings(symbol));
        status.put("enabled", tradingBotService.isBotEnabled(symbol));
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * 거래 설정 조회
     */
    @GetMapping("/trade-settings")
    public ResponseEntity<TradeSettings> getTradeSettings(@RequestParam String symbol) {
        return ResponseEntity.ok(tradingBotService.getTradeSettings(symbol));
    }
    
    /**
     * 거래 설정 업데이트
     */
    @PutMapping("/trade-settings")
    public ResponseEntity<TradeSettings> updateTradeSettings(@RequestBody TradeSettings settings) {
        return ResponseEntity.ok(tradingBotService.updateTradeSettings(settings));
    }
    
    /**
     * 호가창 설정 조회
     */
    @GetMapping("/orderbook-settings")
    public ResponseEntity<OrderBookSettings> getOrderBookSettings(@RequestParam String symbol) {
        return ResponseEntity.ok(tradingBotService.getOrderBookSettings(symbol));
    }
    
    /**
     * 호가창 설정 업데이트
     */
    @PutMapping("/orderbook-settings")
    public ResponseEntity<OrderBookSettings> updateOrderBookSettings(@RequestBody OrderBookSettings settings) {
        return ResponseEntity.ok(tradingBotService.updateOrderBookSettings(settings));
    }
    
    /**
     * 강제 거래 설정 조회
     */
    @GetMapping("/force-trade-settings")
    public ResponseEntity<ForceTradeSettings> getForceTradeSettings(@RequestParam String symbol) {
        return ResponseEntity.ok(tradingBotService.getForceTradeSettings(symbol));
    }
    
    /**
     * 강제 거래 설정 업데이트
     */
    @PutMapping("/force-trade-settings")
    public ResponseEntity<ForceTradeSettings> updateForceTradeSettings(@RequestBody ForceTradeSettings settings) {
        return ResponseEntity.ok(tradingBotService.updateForceTradeSettings(settings));
    }
    
    /**
     * 거래 봇 시작
     */
    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> startBot(@RequestParam String symbol) {
        tradingBotService.startBot(symbol);
        Map<String, String> response = new HashMap<>();
        response.put("status", "started");
        response.put("symbol", symbol);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 거래 봇 중지
     */
    @PostMapping("/stop")
    public ResponseEntity<Map<String, String>> stopBot(@RequestParam String symbol) {
        tradingBotService.stopBot(symbol);
        Map<String, String> response = new HashMap<>();
        response.put("status", "stopped");
        response.put("symbol", symbol);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 강제 종료 활성화
     */
    @PostMapping("/force-stop")
    public ResponseEntity<Map<String, String>> forceStop(@RequestParam String symbol) {
        tradingBotService.forceStop(symbol);
        Map<String, String> response = new HashMap<>();
        response.put("status", "force_stopped");
        response.put("symbol", symbol);
        return ResponseEntity.ok(response);
    }
}