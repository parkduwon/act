package com.act.ldk.controller;

import com.act.ldk.service.dto.TradingInfoResponse;
import com.act.ldk.service.TradingInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/trading")
@RequiredArgsConstructor
public class TradingInfoController {
    
    private final TradingInfoService tradingInfoService;
    
    @GetMapping("/info")
    public ResponseEntity<TradingInfoResponse> getTradingInfo(@RequestParam(defaultValue = "ldk_usdt") String symbol) {
        log.info("Getting trading info for symbol: {}", symbol);
        TradingInfoResponse response = tradingInfoService.getTradingInfo(symbol);
        return ResponseEntity.ok(response);
    }
}