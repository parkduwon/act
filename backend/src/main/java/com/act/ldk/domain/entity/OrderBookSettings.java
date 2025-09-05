package com.act.ldk.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_book_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderBookSettings extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String symbol;  // e.g., "ldk_usdt"
    
    // 매도 호가 설정
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal askOrderBookStartPrice;  // 매도 호가 시작 범위
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal askOrderBookEndPrice;  // 매도 호가 종료 범위
    
    @Column(nullable = false)
    private Integer askOrderBookLimitCount;  // 매도 호가 최대 개수
    
    @Column(nullable = false)
    private Boolean askStopYN;  // 매도 호가 중지 여부
    
    // 매수 호가 설정
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal bidOrderBookStartPrice;  // 매수 호가 시작 범위
    
    @Column(nullable = false, precision = 20, scale = 8)
    private BigDecimal bidOrderBookEndPrice;  // 매수 호가 종료 범위
    
    @Column(nullable = false)
    private Integer bidOrderBookLimitCount;  // 매수 호가 최대 개수
    
    @Column(nullable = false)
    private Boolean bidStopYN;  // 매수 호가 중지 여부
    
    @Column(nullable = false)
    private Boolean enabled;  // 호가창 활성화 여부
    
    @PrePersist
    protected void onCreate() {
        enabled = true;
        askStopYN = false;
        bidStopYN = false;
    }
}