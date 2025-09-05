package com.act.ldk.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "force_trade_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForceTradeSettings extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String symbol;  // e.g., "ldk_usdt"
    
    @Column(nullable = false)
    private Boolean forceStopEnabled;  // 강제 종료 활성화 여부
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ForceType forceType;  // 강제 거래 유형
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderSide forceTradeType;  // 강제 거래 방향
    
    public enum ForceType {
        MAX_PRICE,      // 최고가 도달
        MIN_PRICE,      // 최저가 도달
        MIN_USDT_ASSET, // USDT 부족
        NONE            // 없음
    }
    
    public enum OrderSide {
        BUY,
        SELL,
        NONE
    }
    
    @PrePersist
    protected void onCreate() {
        if (forceStopEnabled == null) {
            forceStopEnabled = false;
        }
        if (forceType == null) {
            forceType = ForceType.NONE;
        }
        if (forceTradeType == null) {
            forceTradeType = OrderSide.NONE;
        }
    }
}