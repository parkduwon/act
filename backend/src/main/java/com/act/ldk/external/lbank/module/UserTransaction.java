package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class UserTransaction {

    private String txUuid;
    private String orderUuid;
    private String tradeType;
    private Long dealTime;
    private BigDecimal dealPrice;
    private BigDecimal dealQuantity;
    private BigDecimal dealVolumePrice;
    private BigDecimal tradeFee;
    private BigDecimal tradeFeeRate;

    @Override
    public String toString() {
        return "UserTransaction{" +
                "txUuid='" + txUuid + '\'' +
                ", orderUuid='" + orderUuid + '\'' +
                ", tradeType='" + tradeType + '\'' +
                ", dealTime=" + dealTime +
                ", dealPrice=" + dealPrice +
                ", dealQuantity=" + dealQuantity +
                ", dealVolumePrice=" + dealVolumePrice +
                ", tradeFee=" + tradeFee +
                ", tradeFeeRate=" + tradeFeeRate +
                '}';
    }
}
