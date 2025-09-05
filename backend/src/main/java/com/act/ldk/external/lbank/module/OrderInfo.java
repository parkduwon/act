package com.act.ldk.external.lbank.module;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderInfo {
    private String symbol;
    private String orderId;
    private String clientOrderId;
    private String price;
    private String origQty;
    private String executedQty;
    private String cummulativeQuoteQty;
    private String status;
    private String type;
    private Long time;
    private Long updateTime;
    private String origQuoteOrderQty;

    @Override
    public String toString() {
        return "OrderInfo{" +
                "symbol='" + symbol + '\'' +
                ", orderId='" + orderId + '\'' +
                ", clientOrderId='" + clientOrderId + '\'' +
                ", price='" + price + '\'' +
                ", origQty='" + origQty + '\'' +
                ", executedQty='" + executedQty + '\'' +
                ", cummulativeQuoteQty='" + cummulativeQuoteQty + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                ", updateTime=" + updateTime +
                ", origQuoteOrderQty='" + origQuoteOrderQty + '\'' +
                '}';
    }
}
