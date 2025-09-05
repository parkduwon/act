package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CancelOrder {
    private String symbol;
    private String origClientOrderId;
    private String orderId;
    private String price;
    private String origQty;
    private String executedQty;
    private Integer status;
    private String timeInForce;
    private String tradeType;

    @Override
    public String toString() {
        return "CancelOrder{" +
                "symbol='" + symbol + '\'' +
                ", origClientOrderId='" + origClientOrderId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", price='" + price + '\'' +
                ", origQty='" + origQty + '\'' +
                ", executedQty='" + executedQty + '\'' +
                ", status=" + status +
                ", timeInForce='" + timeInForce + '\'' +
                ", tradeType='" + tradeType + '\'' +
                '}';
    }
}
